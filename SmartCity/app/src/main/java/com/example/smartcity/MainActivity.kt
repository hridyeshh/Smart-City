import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.TrafficModel
import com.google.maps.model.TravelMode


class MapsActivity : ComponentActivity() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = GeoApiContext.Builder()
                        .apiKey("AIzaSyCTcgkAxQaZ_d5XjqtmqHbKu3g0HJxk88A")
                        .build()
                    val directionsApiRequest = DirectionsApiRequest(context)
                    val origin = LatLng(37.7749, -122.4194) // San Francisco
                    val destination = LatLng(34.0522, -118.2437) // Los Angeles
                    directionsApiRequest.origin(com.google.maps.model.LatLng(37.7749, -122.4194))
                    directionsApiRequest.destination(com.google.maps.model.LatLng(34.0522, -118.2437))

                    mapView = MapView(this)
                    mapView.onCreate(savedInstanceState)
                    mapView.getMapAsync { googleMap ->
                        googleMap.uiSettings.isZoomControlsEnabled = true
                        googleMap.isTrafficEnabled = true
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 5f))

                        val directionsResult = directionsApiRequest.awaitIgnoreError()
                        if (directionsResult != null && directionsResult.routes.isNotEmpty()) {
                            val route = directionsResult.routes[0]
                            route.legs.forEach { leg ->
                                leg.steps.forEach { step ->
                                    step.startLocation?.let {
                                        googleMap.addPolyline(
                                            PolylineOptions()
                                                .add(origin)
                                                .add(destination)
                                                .width(5f)
                                                .color(Color.Blue.hashCode()) // Change to lowercase 'blue'
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}

