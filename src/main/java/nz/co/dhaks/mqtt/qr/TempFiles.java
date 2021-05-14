package nz.co.dhaks.mqtt.qr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
// Manage temp files
public class TempFiles {



	public static void writeAsQR(String data, Path path) throws WriterException, IOException {

 
		Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

		hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		// Create the QR code and save
		// in the specified folder
		// as a jpg file

		BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes( Constants.CHARSET),  Constants.CHARSET),
				BarcodeFormat.QR_CODE, Constants.DEFAULT_WIDTH,  Constants.DEFAULT_HEIGHT);

		MatrixToImageWriter.writeToPath(matrix, 
				path.toString().substring(path.toString().lastIndexOf('.') + 1), // extension
				path);
	}

	public static String writeAsString(byte[] content) {

		try {
			// create a temporary file
			Path tempFile = Files.createTempFile(null, null);
			System.out.println(tempFile);
			// write a line
			Files.write(tempFile, content);
			return tempFile.toAbsolutePath().toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static String readQRFile(String fileName) throws FileNotFoundException, IOException, NotFoundException {

		Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

		hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		BinaryBitmap binaryBitmap = new BinaryBitmap(
				new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(fileName)))));

		Result result = new MultiFormatReader().decode(binaryBitmap);

		return result.getText();

	}

}
