This library is implemented originally by Katsunori Kanda [potix2/spark-google-spreadsheets](https://github.com/potix2/spark-google-spreadsheets) and all benefits for it should be addressed to him.

The changes which were introduced in this fork:

1. Usage OAuth 2.0 to access Google APIs;
2. Upgrade Spark to version 3.1.1;
3. Miscellaneous code improvements.

# Spark Google Spreadsheets

Google Spreadsheets datasource for [SparkSQL and DataFrames](http://spark.apache.org/docs/latest/sql-programming-guide.html)

[![Actions Build](https://github.com/riskidentdms/spark-google-spreadsheets/actions/workflows/scala.yml/badge.svg)](https://github.com/riskidentdms/spark-google-spreadsheets/actions)

## Notice

Before you start using this library, please read the [Introduction to the Google Sheets API v4](https://developers.google.com/sheets/guides/concepts)
to understand all basic concepts.

## Requirements

### Latest compatible versions

| This library | Spark Version |
|--------------| ------------- |
| 0.1.1        | 3.1.1  |

## Linking

Using SBT:

```
libraryDependencies += "com.github.riskidentdms" %% "spark-google-spreadsheets" % "0.1.1"
```

Using Maven:

```xml
<dependency>
  <groupId>com.github.riskidentdms</groupId>
  <artifactId>spark-google-spreadsheets_2.12</artifactId>
  <version>0.1.1</version>
</dependency>
```

## Using Google application credentials
This library uses OAuth 2.0 to access Google APIs: [Using OAuth 2.0 to Access Google APIs](https://developers.google.com/identity/protocols/oauth2)

Please read this article in order to set up OAuth 2.0 in your Google Service Account: [Setting up OAuth 2.0](https://support.google.com/cloud/answer/6158849)

Keep in mind that you have to use the JSON key type, when you create a Service Account key.
A JSON file that contains the private key should be downloaded and stored securely because this key can't be recovered if lost.

There are two ways of providing authentication credentials to your application code namely:

- by providing the path to the JSON file that contains private key described above

```scala
import com.github.riskidentdms.spark.google.spreadsheets.Credentials
val credentials = Credentials.credentialsFromFile("path_to_key_json")
```
or by adding an input option for the underlying data source

```scala
.option("credentialsPath", "path_to_key_json")
```

```sql
OPTIONS(credentialsPath "path_to_key_json")
```

- by providing JSON String that contains private key described above

```scala
import com.github.riskidentdms.spark.google.spreadsheets.Credentials
Credentials.credentialsFromJsonString("json_string")
```

```scala
.option("credentialsJson", "json_key")
```

```sql
OPTIONS(credentialsJson 'json_key')
```

## Usage examples
### SQL API

```sql
CREATE TABLE cars
USING com.github.riskidentdms.spark.google.spreadsheets
OPTIONS (
    path "<spreadsheetId>/worksheet1",
    credentialsPath "path_to_key_json"
)
```

### Scala API

```scala
import org.apache.spark.sql.SparkSession

val sqlContext = SparkSession.builder()
  .master("local[2]")
  .appName("SpreadsheetSuite")
  .getOrCreate().sqlContext

// Creates a DataFrame from a specified worksheet
val df = sqlContext.read.
    format("com.github.riskidentdms.spark.google.spreadsheets")
    .option("credentialsPath", "path_to_key_json")
    .load("<spreadsheetId>/worksheet1")

// Saves a DataFrame to a new worksheet
df.write.
    format("com.github.riskidentdms.spark.google.spreadsheets")
    .option("credentialsPath", "path_to_key_json")
    .save("<spreadsheetId>/newWorksheet")

```

```scala
import org.apache.spark.sql.SparkSession

val sqlContext = SparkSession.builder()
  .master("local[*]")
  .appName("SpreadsheetSuite")
  .getOrCreate().sqlContext

// Creates a DataFrame from a specified worksheet
val df = sqlContext.read
    .format("com.github.riskidentdms.spark.google.spreadsheets")
    .option("credentialsPath", "path_to_key_json")
    .load("<spreadsheetId>/worksheet1")
```

More details: https://cloud.google.com/docs/authentication/production

## Local testing

You have to do some preparations in order to be able to run tests from your machine:

1. Upload `files/SpreadsheetSuite.xlsx` to the [Google Spreadsheet](https://docs.google.com/spreadsheets).

2. Export the spreadsheet ID of previously uploaded document as `TEST_SPREADSHEET_ID` environment variable.
The spreadsheet ID you can find in the URL of the opened document. The pattern of the URL looks like that:

`https://docs.google.com/spreadsheets/d/<SPREADSHEET_ID>`

3. Export the JSON private key as `OAUTH_JSON` environment variable.
Please see details [here](<#Using Google application credentials>).

## License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
