import com.bat.petra.photorecognition.worker.WorkerApplication
import com.bat.petra.photorecognition.worker.service.AzureService
import com.bat.petra.photorecognition.worker.service.FaceDetectionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Ignore
import spock.lang.Specification

@SpringBootTest(classes = WorkerApplication.class)
class FaceDetectionSpec extends Specification {

    @Autowired
    FaceDetectionService service

    @Autowired
    AzureService azureService

    @Ignore
    def "given set with face images should return set without those images"() {

        given:
        def urls = azureService.getBlobsUrlsFromAzure()

        when:
        def result = service.detectFaces(azureService.getBlobsFromAzure(urls))

        then:
        urls.size() > result.size()
    }
}
