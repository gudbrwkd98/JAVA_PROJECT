package common.image;

public class FileManager {
	public static String getFilename(String path) {
		path = path.substring(path.lastIndexOf('/')+1,path.length());
		return path;
	}
	
	public static String getExtend(String filename) {
		filename = filename.substring(filename.lastIndexOf('.')+1,filename.length());
		return filename;
	}
	
}
