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

import com.google.api.services.sheets.v4.model.{CellData, ExtendedValue, RowData}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DataTypes, StructType}

import scala.collection.JavaConverters._
import scala.collection.breakOut
import scala.language.postfixOps

object Util {

  def toRowData(row: Row): RowData =
      new RowData().setValues(
        row.schema.fields.zipWithIndex.map { case (f, i) =>
          new CellData()
            .setUserEnteredValue(
              f.dataType match {
                case DataTypes.StringType => new ExtendedValue().setStringValue(row.getString(i))
                case DataTypes.LongType => new ExtendedValue().setNumberValue(row.getLong(i).toDouble)
                case DataTypes.IntegerType => new ExtendedValue().setNumberValue(row.getInt(i).toDouble)
                case DataTypes.FloatType => new ExtendedValue().setNumberValue(row.getFloat(i).toDouble)
                case DataTypes.BooleanType => new ExtendedValue().setBoolValue(row.getBoolean(i))
                case DataTypes.DateType => new ExtendedValue().setStringValue(row.getDate(i).toString)
                case DataTypes.ShortType => new ExtendedValue().setNumberValue(row.getShort(i).toDouble)
                case DataTypes.TimestampType => new ExtendedValue().setStringValue(row.getTimestamp(i).toString)
                case DataTypes.DoubleType => new ExtendedValue().setNumberValue(row.getDouble(i))
              }
            )
        }(breakOut).asJava
      )
}
