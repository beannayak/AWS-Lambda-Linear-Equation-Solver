package com.rasal.lindowrapper;

import java.io.File;
import java.lang.reflect.Field;

import com.lindo.Lindo;
import com.rasal.awslambda.errorhandling.LindoResponseValidator;
import com.springmock.Autowired;
import com.springmock.Component;

@Component
public class LindoEnvironmentSetup {

	private static final String LESolverBaseLocation = getLESolverBaseLocation();
	static {
		addDirectoryInPath(LESolverBaseLocation);
		System.loadLibrary("lindojni");
	}
	
	private static Object nativeEnv;
	
	@Autowired
	private LindoResponseValidator lindoResponseValidator;
	
	public Object getEnv() {
		if (nativeEnv == null) {
			nativeEnv = loadNativeEnvironment();
		}
		return nativeEnv;
	}
	
	private Object loadNativeEnvironment() {
		StringBuffer licenseKey = loadLicense();
		return createEnvironment(licenseKey);
	}

	private Object createEnvironment(StringBuffer licenseKeyHolder) {
		int environmentCreationResponse[] = new int[1];
		Object nativeLindoEnvironment = Lindo.LScreateEnv(environmentCreationResponse, licenseKeyHolder.toString());
		lindoResponseValidator.validateLindoCreatedObjectAndResponse(nativeLindoEnvironment, environmentCreationResponse[0], "Failed to create native LINDO environment!");
		
		return nativeLindoEnvironment;
	}

	private StringBuffer loadLicense() {
		StringBuffer licenseKey = new StringBuffer(10000);
		int lindoKeyRetrievalResponse = Lindo.LSloadLicenseString(LESolverBaseLocation+ "/lndapi120.lic", licenseKey);
		lindoResponseValidator.validateLindoResponse(lindoKeyRetrievalResponse, "License key retrieval failed!");
		
		return licenseKey;
	}
	
	private static String getLESolverBaseLocation() {
		String region = System.getenv("AWS_REGION");
		String returnedRegion;
		if (region == null) {
			returnedRegion = System.getProperty("user.dir") + "/src/main/resources/lib/LindoAPI/unix";
		} else {
			returnedRegion = "/var/task/lib/LindoAPI/unix";
		}
		
		return returnedRegion;
	}
	
	public static void addDirectoryInPath(String directoryNameWithPath) {
		try {
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[]) field.get(null);
			for (String path : paths) {
				if (directoryNameWithPath.equals(path)) {
					return;
				}
			}
			String[] tmp = new String[paths.length + 1];
			System.arraycopy(paths, 0, tmp, 0, paths.length);
			tmp[paths.length] = directoryNameWithPath;
			field.set(null, tmp);
			System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + directoryNameWithPath);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new RuntimeException("Failed to get permissions to set library path");
		}
	}
}
