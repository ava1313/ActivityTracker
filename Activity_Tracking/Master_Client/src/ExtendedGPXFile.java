import GPXFile.GPXFile;

public class ExtendedGPXFile extends GPXFile {
    private String filename;
    private byte[] fileContent;

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public byte[] getFileContent() {
        return fileContent;
    }
}

