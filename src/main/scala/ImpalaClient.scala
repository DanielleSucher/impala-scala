package com.stripe.impalascala

import org.apache.thrift.transport.TSocket
import org.apache.thrift.protocol.TBinaryProtocol

import com.cloudera.impala.thrift.ImpalaService.{Client => ClouderaImpalaClient}
import com.cloudera.beeswax.api._

import scala.collection.JavaConversions._

case class ImpalaClient(url: String, port: Int) {
  lazy val client = {
    val socket = new TSocket(url, port)
    socket.open

    val client = new ClouderaImpalaClient(new TBinaryProtocol(socket))
    client.ResetCatalog

    client
  }

  def execute(statement: String) {
    query(statement){row => println(row)}
  }

  def query(statement: String)(fn: Array[String] => Unit) {
    println(statement)

    val query = new Query
    query.query = statement

    val handle = client.query(query)

    var finished = false
    while (!finished) {
      val results = client.fetch(handle, false, -1)
      if(results.ready) {
        results.data.foreach{row => fn(row.split("\\s+"))}
        if(!results.has_more)
          finished = true
      } else {
        Thread.sleep(100L)
      }
    }

    client.close(handle)
  }
}

