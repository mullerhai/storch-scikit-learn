/*
 * Storch feature engineer etl  project base  power by Spotify
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package torch.sklearn.feature.json

import io.circe.{Decoder, Encoder, Error, Json}
import io.circe.parser

private[json] trait JsonEncoder {
  final def encode[T: Encoder](t: T): Json = Encoder[T].apply(t)
}

private[json] trait JsonDecoder {
  final def decode[T: Decoder](str: String): Either[Error, T] = parser.decode[T](str)
}
