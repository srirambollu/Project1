import com.bat.petra.photorecognition.worker.WorkerApplication
import com.bat.petra.photorecognition.worker.service.AzureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = WorkerApplication.class)
class AzureServiceSpec extends Specification {

    @Autowired
    AzureService service

    def "should get list of URLs of blobs from Azure"() {

        when:
        Set<URL> result = service.getBlobsUrlsFromAzure()

        then:
        Optional.ofNullable(result).isPresent()
        result.size() > 0
    }

    def "given urls, should get blobs from Azure"() {

        given:
        Set<URL> urls = service.getBlobsUrlsFromAzure()

        when:
        Set<ByteArrayOutputStream> result = service.getBlobsFromAzure(urls)

        then:
        Optional.ofNullable(result).isPresent()
        result.size() > 0
    }
}
