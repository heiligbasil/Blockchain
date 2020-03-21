package com.lambdaschool.blockchain_wallet.ui

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Blockchain {
    @SerializedName("chain")
    @Expose
    var chain: List<Chain>? = null

    @SerializedName("length")
    @Expose
    var length: Int? = null
}


class Chain {
    @SerializedName("index")
    @Expose
    var index: Int? = null

    @SerializedName("previous_hash")
    @Expose
    var previousHash: String? = null

    @SerializedName("proof")
    @Expose
    var proof: Int? = null

    @SerializedName("timestamp")
    @Expose
    var timestamp: Double? = null

    @SerializedName("transactions")
    @Expose
    var transactions: List<Transaction>? = null
}


class Transaction {
    @SerializedName("amount")
    @Expose
    var amount: Float? = null

    @SerializedName("recipient")
    @Expose
    var recipient: String? = null

    @SerializedName("sender")
    @Expose
    var sender: String? = null
}

object UserId {
    var userId: String? = null
    var balance: Float? = 0f
}