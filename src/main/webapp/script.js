// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/** Creates a map that shows a single marker. */
jQuery(document).ready(function(){
    var i = 0;
    const arrLength = 5;
    getLength();
    while (i < arrLength) {
        getPosts(i);
        i++;
    }
});

function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 37.422, lng: -122.084}, zoom: 16});

  const trexMarker = new google.maps.Marker({
    position: {lat: 37.421903, lng: -122.084674},
    map: map,
    title: 'Stan the T-Rex'
  });
}

function getLength(){
    fetch('/data').then(response => response.json()).then((strArr) => {
        arrLength = strArr.length;
    });
}

function getPosts(number = i) {
  fetch('/data').then(response => response.json()).then((strArr) => {
    document.getElementById('post_' + number.toString()).innerText = strArr[number];
  });
}
