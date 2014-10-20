# impala-scala

This is a scala client for [Cloudera Impala][1]. You can use it like this:

```scala
import com.daniellesucher.impala.{Cursor, ImpalaClient}

val client = ImpalaClient("host", 21000)
client.execute("SHOW TABLES")
// => merchants users...
```

You can also use a cursors to iterate over the rows. This lets you pass in any function you like, and avoid loading the entire result set into memory at once.

```scala
import com.daniellesucher.impala.{Cursor, ImpalaClient}

val client = ImpalaClient("host", 21000)
val cursor = client.query("SELECT zip, income FROM zipcode_incomes ORDER BY income DESC")

cursor.foreach { row: Seq[ImpalaValue] =>
  // whatever makes you happy
}

cursor.close
```

[1]: http://www.cloudera.com/content/cloudera/en/documentation/cdh5/v5-0-0/Impala/impala.html
