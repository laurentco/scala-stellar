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

object AccountValidation extends App {

  def run() = {
    val server = FederationServer("https://horizon-testnet.stellar.org")

    val keyPair = KeyPair.random

    for {
      resp1     <- TestNetwork.fund(keyPair) if resp1.isSuccess
      acct      <- TestNetwork.account(keyPair)
      validated <- server.byAccount(PublicKey(keyPair.pk))
    } yield {
      println("Validated: " + validated)
    }
  }

  Await.result(run(), 1 minute)
}
