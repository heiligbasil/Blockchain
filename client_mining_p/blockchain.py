import hashlib
import json
from time import time
from uuid import uuid4

from flask import Flask, jsonify, request


class Blockchain(object):
    def __init__(self):
        self.chain = []
        self.current_transactions = []
        # Create the genesis block
        self.new_block(previous_hash=1, proof=100)

    def new_block(self, proof, previous_hash=None):
        """
        Create a new Block in the Blockchain
        A block should have:
        * Index
        * Timestamp
        * List of current transactions
        * The proof used to mine this block
        * The hash of the previous block

        :param proof: <int> The proof given by the Proof of Work algorithm
        :param previous_hash: (Optional) <str> Hash of previous Block
        :return: <dict> New Block
        """
        block = {
            'index': len(self.chain) + 1,
            'timestamp': time(),
            'transactions': self.current_transactions,
            'proof': proof,
            'previous_hash': previous_hash or self.hash(self.last_block)
        }
        # Reset the current list of transactions
        self.current_transactions = []
        # Append the block to the chain
        self.chain.append(block)
        # Return the new block
        return block

    def hash(self, block):
        """
        Creates a SHA-256 hash of a Block

        :param block: <dict> Block
        :return: hex_hash: <str> hexdigest()
        """
        # Use json.dumps to convert json into a string
        # Use hashlib.sha256 to create a hash
        # It requires a `bytes-like` object, which is what
        # .encode() does.
        # It converts the Python string into a byte string.
        # We must make sure that the Dictionary is Ordered,
        # or we'll have inconsistent hashes
        string_block = json.dumps(block, sort_keys=True)
        raw_hash = hashlib.sha256(string_block.encode())
        hex_hash = raw_hash.hexdigest()
        # By itself, the sha256 function returns the hash in a raw string
        # that will likely include escaped characters.
        # This can be hard to read, but .hexdigest() converts the
        # hash to a string of hexadecimal characters, which is
        # easier to work with and understand
        return hex_hash

    @property
    def last_block(self):
        return self.chain[-1]

    @staticmethod
    def valid_proof(block_string, proof):
        """
        Validates the Proof: Does SHA256 hash(block_string + proof) contain 6 leading zeroes?

        :param block_string: <string> The stringified block to use to check in combination with `proof`
        :param proof: <int?> The value that when combined with the stringified previous block results in a hash that has the correct number of leading zeroes.
        :return: True if the resulting hash is a valid proof; False otherwise
        """
        guess = f'{block_string}{proof}'.encode()
        guess_hash = hashlib.sha256(guess).hexdigest()
        return guess_hash[:3] == '000'


# Instantiate our Node
app = Flask(__name__)

# Generate a globally unique address for this node
node_identifier = str(uuid4()).replace('-', '')

# Instantiate the Blockchain
blockchain = Blockchain()


# curl -i -X POST -d '{"proof":"328347392029478565858","id":"basil"}' http://localhost:5000/mine
@app.route('/mine', methods=['POST'])
def mine():
    print('Endpoint: /mine', end=' ')
    data = request.get_json()
    response_code = 400
    if data and 'proof' in data and 'id' in data:
        sorted_block = json.dumps(blockchain.last_block, sort_keys=True)
        mined_message = 'Nothing Forged!'
        if blockchain.valid_proof(sorted_block, data['proof']):
            # Forge the new block by adding it to the chain with the proof
            previous_hash = blockchain.hash(sorted_block)
            blockchain.new_block(data['proof'], previous_hash)
            mined_message = 'New Block Forged'
        response = {
            'message': mined_message
        }
        response_code = 200
    else:
        response = {
            'error': 'The `proof` and/or `id` attributes were not found!'
        }
    # TODO: Remember, a valid proof should fail for all senders except the first
    print(response)
    return jsonify(response), response_code


# curl -X GET http://localhost:5000/chain | python -m json.tool
@app.route('/chain', methods=['GET'])
def full_chain():
    print('Endpoint: /chain', end=' ')
    response = {
        'chain': blockchain.chain,
        'length': len(blockchain.chain)
    }
    print(response)
    return jsonify(response), 200


# curl -i -X GET http://localhost:5000/last_block
@app.route('/last_block', methods=['GET'])
def get_last_block():
    print('Endpoint: /last_block', end=' ')
    response = {
        'last_block': blockchain.last_block
    }
    print(response)
    return jsonify(response), 200


# Run the Flask server on the specified port
if __name__ == '__main__':
    app.run(host='localhost', port=5000, debug=True)
