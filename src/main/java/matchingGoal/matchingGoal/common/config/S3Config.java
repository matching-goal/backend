package matchingGoal.matchingGoal.common.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
  @Value("${cloud.aws.s3.credentials.accessKey}")
  private String accessKey;

  @Value("${cloud.aws.s3.credentials.secretKey}")
  private String secretKey;

  @Value("${cloud.aws.s3.region.static}")
  private String region;

  @Bean
  public AmazonS3Client amazonS3Client() {

    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

    return (AmazonS3Client) AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .build();
  }
}