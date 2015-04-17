package poke.resources.data.DAO;

import java.io.File;
import java.io.FileOutputStream;

import poke.resources.ImageResource;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

public class ImageStoreS3 {
	private static String bucketName = "cmpe275";

	public boolean uploadImage(String keyName) {
		String uploadFileName = ImageResource.imagePath + keyName + ".png";

		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		boolean result = true;

		try {
			System.out.println("Uploading file...");
			
			File file = new File(uploadFileName);
			
			s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
			file.delete();

			System.out.println("File uploaded successfully!!");
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which "
					+ "means your request made it "
					+ "to Amazon S3, but was rejected with an error response"
					+ " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			result = false;
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which "
					+ "means the client encountered "
					+ "an internal error while trying to "
					+ "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
			result = false;
		} finally {
			return result;
		}
	}

	public boolean getImage(String keyName) {
		String downloadFileName = ImageResource.imagePath + keyName + ".png";
		
		AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
		boolean result = true;
		try {
			
			System.out.println("Downloading file...");
			GetObjectRequest request = new GetObjectRequest(bucketName, keyName);
			S3Object object = s3Client.getObject(request);
			S3ObjectInputStream objectContent = object.getObjectContent();
			IOUtils.copy(objectContent, new FileOutputStream(downloadFileName));

			S3Object s3object = s3Client.getObject(new GetObjectRequest(
					bucketName, keyName));

			System.out.println("File downloaded successfully!!");

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which"
					+ " means your request made it "
					+ "to Amazon S3, but was rejected with an error response"
					+ " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			result = false;
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means"
					+ " the client encountered "
					+ "an internal error while trying to "
					+ "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
			result = false;
		} catch (Exception e) {
			System.out.println(e);
			result = false;
		} finally {
			return result;
		}
	}
}
