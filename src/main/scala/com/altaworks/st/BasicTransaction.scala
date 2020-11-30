package com.altaworks.st

import stellar.sdk._
import stellar.sdk.model.TimeBounds
import stellar.sdk.model.op.PaymentOperation
import stellar.sdk.model.Amount
import stellar.sdk.model.Transaction
import stellar.sdk.model.response.FederationResponse
import stellar.sdk.model.AccountId
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object BasicTransaction extends App {

  def run() = {
    val server = FederationServer("https://horizon-testnet.stellar.org")

    val payerKeyPair = KeyPair.random
    val payeeKeyPair = KeyPair.random

    for {
      resp1        <- TestNetwork.fund(payerKeyPair) if resp1.isSuccess
      resp2        <- TestNetwork.fund(payeeKeyPair) if resp2.isSuccess
      payerAccount <- TestNetwork.account(payerKeyPair)
      payeeAccount <- TestNetwork.account(payeeKeyPair)
      _            <- server.byAccount(PublicKey(payeeKeyPair.pk))
      resp3 <- Transaction(payerAccount.toAccount, timeBounds = TimeBounds.Unbounded, maxFee = Amount.lumens(100))(TestNetwork)
        .add(new PaymentOperation(payeeAccount.toAccount.id, Amount.lumens(5000)))
        .sign(payerKeyPair)
        .submit() if resp3.isSuccess
      updPayerAccount <- TestNetwork.account(payerKeyPair)
      updPayeeAccount <- TestNetwork.account(payeeKeyPair)
    } yield {
      println("Payer balances: " + payerAccount.balances)
      println("Payee balances: " + payeeAccount.balances)
      println("Updated Payer balances: " + updPayerAccount.balances)
      println("Updated Payee balances: " + updPayeeAccount.balances)
    }
  }

  Await.result(run(), 1 minute)
}
