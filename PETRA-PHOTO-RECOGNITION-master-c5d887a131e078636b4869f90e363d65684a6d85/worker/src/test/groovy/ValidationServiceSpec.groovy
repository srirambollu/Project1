import com.bat.petra.photorecognition.worker.WorkerApplication
import com.bat.petra.photorecognition.worker.service.AzureService
import com.bat.petra.photorecognition.worker.service.ValidationService
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifSubIFDDirectory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneId

@SpringBootTest(classes = WorkerApplication.class)
class ValidationServiceSpec extends Specification{

    final int FIVE_MEGABYTES_SIZE = 5_000_000
    final long NUMBER_OF_HOURS_BEFORE_EXPIRATION = 24

    @Autowired
    ValidationService service

    @Autowired
    AzureService azureService

    @Ignore
    def "given set of images with larger than 5 MB should return set without this images"() {

        given:
        def urls = azureService.getBlobsUrlsFromAzure()

        when:
        def result = service.validateSize(azureService.getBlobsFromAzure(urls))

        then:
        sizeEvaluationHelper(result)
    }

    @Ignore
    def "given set of images with some older than 24 hours should return set without this images"() {

        given:
        def urls = azureService.getBlobsUrlsFromAzure()

        when:
        def result = service.validateMetadata(azureService.getBlobsFromAzure(urls))

        then:
        sizeEvaluationHelper(result)
    }

    //method for checking whether all images are below 5 MB
    boolean sizeEvaluationHelper(Set<ByteArrayOutputStream> toEvaluate) {

        for(ByteArrayOutputStream stream : toEvaluate) {

            if(stream.size() > FIVE_MEGABYTES_SIZE) {
                return false
            }
        }

        return true
    }

     //method for checking whether all images are below 5 MB
     boolean metadataEvaluationHelper(Set<ByteArrayOutputStream> toEvaluate) {

         for(ByteArrayOutputStream stream : toEvaluate) {

             Metadata metadata = ImageMetadataReader.readMetadata(
                     new ByteArrayInputStream(stream.toByteArray())
             )
             ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)
             Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)

             LocalDateTime now = LocalDateTime.now()
             LocalDateTime yesterday = now.minusHours(24)
             LocalDateTime metadataDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

             if(metadataDate <= yesterday) {
                 return false
             }
         }

         return true
     }
}
