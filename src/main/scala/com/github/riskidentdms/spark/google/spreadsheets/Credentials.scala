/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.riskidentdms.spark.google.spreadsheets

import com.google.api.services.sheets.v4.SheetsScopes
import com.google.auth.oauth2.{GoogleCredentials, OAuth2Credentials}

import java.time.Duration
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.io.Source

object Credentials {
  private val scopes = List(SheetsScopes.SPREADSHEETS)

  def credentialsFromFile(file: String): OAuth2Credentials = {
    val lines = Source.fromFile(file)
    try {
      credentialsFromJsonString(lines.getLines().mkString)
    } finally {
      lines.close()
    }
  }

  def credentialsFromJsonString(oauth2JSON: String): OAuth2Credentials = {
    val credentials: GoogleCredentials = GoogleCredentials.fromStream(
      new java.io.ByteArrayInputStream(oauth2JSON.getBytes(java.nio.charset.StandardCharsets.UTF_8))
    ).createScoped(scopes.asJava)

    credentials.refreshIfExpired()
    val accessToken = credentials.refreshAccessToken()

    GoogleCredentials.newBuilder()
      .setAccessToken(accessToken)
      .setRefreshMargin(Duration.ofDays(1))
      .build()
  }
}
