package visual.controls.center.materialPage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageIconRefference {
	private static ImageIconRefference instance = null;
	
	private List<BufferedImage> extension;
	private BufferedImage canceled;
	private BufferedImage select;
	private BufferedImage reference;
	
	public static ImageIconRefference getInstance(){
		if(instance == null)
			instance = new ImageIconRefference();
		return instance;
	}
	
	private ImageIconRefference(){		
		try {
			canceled = ImageIO.read(getClass().getResource("../../../icons/canceled.png"));
			select = ImageIO.read(getClass().getResource("../../../icons/select.png"));
			reference = ImageIO.read(getClass().getResource("../../../icons/reference.png"));
			
			extension = new ArrayList<BufferedImage>(10);
			
			extension.add(FileExtension.DEFAULT.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/default.png")));
			extension.add(FileExtension.FOLDER.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/folder.png")));
			extension.add(FileExtension.DOC.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/doc.png")));
			extension.add(FileExtension.DOCX.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/docx.png")));
			extension.add(FileExtension.PPT.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/ppt.png")));
			extension.add(FileExtension.PPTX.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/pptx.png")));
			extension.add(FileExtension.RAR.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/rar.png")));
			extension.add(FileExtension.ZIP.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/zip.png")));
			extension.add(FileExtension.HTML.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/html.png")));
			extension.add(FileExtension.PDF.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/pdf.png")));
			extension.add(FileExtension.CHM.ordinal(), ImageIO.read(getClass().getResource("../../../icons/files/chm.png")));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BufferedImage get(FileExtension type) {
		return extension.get(type.ordinal());
	}
	
	public BufferedImage getCanceledImg(){
		return canceled;
	}
	public BufferedImage getSelectImg(){
		return select;
	}
	public BufferedImage getReferenceImg(){
		return reference;
	}
	
}
