import hashlib
import requests

import sys
import json


def proof_of_work(block):
    """
    Simple Proof of Work Algorithm: Stringify the block and look for a proof.
    Loop through possibilities, checking each one against `valid_proof` in an effort to find a number that is a valid proof

    :param block: <str> The data to be stringified
    :return: A valid proof for the provided block
    """
    block_string = json.dumps(block, sort_keys=True)
    proof = 0
    while valid_proof(block_string, proof) is False:
        proof += 1
    return proof


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


if __name__ == '__main__':
    # What is the server address? i.e. `python miner.py https://server.com/api/`
    if len(sys.argv) > 1:
        node = sys.argv[1]
    else:
        node = "http://localhost:5000"

    # Load ID
    f = open('my_id.txt', 'r')
    my_id = f.read()
    print(f'ID is: {my_id}')
    f.close()

    mined_coins = 0

    # Run forever until interrupted
    while True:
        print('Retrieving the last block...')
        r = requests.get(url=node + "/last_block")
        # Handle non-json response
        try:
            data = r.json()
        except ValueError:
            print(f'ERROR: Non-Json response returned: {r}')
            break

        print('Proofing the block...')
        new_proof = proof_of_work(data['last_block'])

        # When found, POST it to the server {"proof": new_proof, "id": my_id}
        post_data = {"proof": new_proof, "id": my_id}
        r = requests.post(url=node + "/mine", json=post_data)
        data = r.json()

        # If the server responds with a message: 'New Block Forged'
        # add 1 to the number of coins mined and print it. Otherwise,
        # print the message from the server.
        if data['message'] == 'New Block Forged':
            mined_coins += 1
            print(f'You have now mined {mined_coins}¢ worth of coins!')
        else:
            print(data['message'])
