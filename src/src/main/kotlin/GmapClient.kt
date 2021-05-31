import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class GmapClient {

    val domainGmap = "https://gmap-marker-tool-be.ghtk.vn"
    val token = ""
    val clientGmap = HttpClient(CIO)
    var onFinishRequestListener : OnFinishRequest? = null
    set(value) {
        field = value
    }

    companion object {
        val requestGetWays = "/api/v1/ways"
    }

    suspend fun getWays(listener : OnFinishRequest){
        val response: HttpResponse = clientGmap.get(domainGmap+requestGetWays) {
            headers {
                append(HttpHeaders.Authorization,
                    "Bearer c_ij3813jdckzl5il4qph2rdqsdnj1zeeuzwhrl2kanfdbvkz2dymgexbbzksxrrgl")
            }


        }


    }

    interface OnFinishRequest{
       fun onSuccess()
       fun onError()
    }

}