package utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import library.sort.Sort;
import models.ResearchGroup;
import models.ResearchGroupWithProperty;
import models.ScientificCategory;
import models.ScientificCategoryWithProperty;
import models.TeachingCategory;
import models.TeachingCategoryWithProperty;
import models.TeachingGroup;
import models.TeachingGroupWithProperty;
import models.materials.MaterialWithProperty;
import sun.misc.BASE64Encoder;
import visual.controls.center.materialPage.FileExtension;

public class Utilities {

	public static javafx.scene.image.Image createImage(BufferedImage image) throws IOException {
	    if (!(image instanceof RenderedImage)) {
	      BufferedImage bufferedImage = new BufferedImage(image.getWidth(),
	              image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	      Graphics g = bufferedImage.createGraphics();
	      g.drawImage(image, 0, 0, null);
	      g.dispose();
	 
	      image = bufferedImage;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ImageIO.write((RenderedImage) image, "png", out);
	    out.flush();
	    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
	    return new javafx.scene.image.Image(in);
	}
	
	public static BufferedImage createBufferedImage(java.awt.Image image){
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	      
	    return bufferedImage;
	}
	
	public static java.awt.Image scaleImageIcon(BufferedImage icon, int width, int height){
		java.awt.Image image = null;
		if(icon.getWidth() != width || icon.getHeight() != height)
			image = icon.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
		
		return image;
	}
	
	public static boolean isEmpty(String text){
		int i = 0;
		
		while(i < text.length() && text.charAt(i) == ' ')
			i++;
		
		return i == text.length() ? true : false;
	}
	
	public static List<TeachingGroupWithProperty> toPropertiesTG(List<TeachingGroup> list){
		List<TeachingGroupWithProperty> list2 = new LinkedList<TeachingGroupWithProperty>();
		
		for(TeachingGroup tg : list)
			list2.add(new TeachingGroupWithProperty(tg));

		return list2;
	}
	
	public static List<ResearchGroupWithProperty> toPropertiesRG(List<ResearchGroup> list){
		List<ResearchGroupWithProperty> list2 = new LinkedList<ResearchGroupWithProperty>();
		
		for(ResearchGroup tg : list)
			list2.add(new ResearchGroupWithProperty(tg));

		return list2;
	}
	
	public static List<TeachingCategoryWithProperty> toPropertiesTC(List<TeachingCategory> list){
		List<TeachingCategoryWithProperty> list2 = new LinkedList<TeachingCategoryWithProperty>();
		
		for(TeachingCategory tg : list)
			list2.add(new TeachingCategoryWithProperty(tg));

		return list2;
	}
	
	public static List<ScientificCategoryWithProperty> toPropertiesSC(List<ScientificCategory> list){
		List<ScientificCategoryWithProperty> list2 = new LinkedList<ScientificCategoryWithProperty>();
		
		for(ScientificCategory tg : list)
			list2.add(new ScientificCategoryWithProperty(tg));

		return list2;
	}
	
	public static FileExtension getFileExtension(String path){
		FileExtension ext = null;
		path = path.toLowerCase();
		ArrayList<Character> chars = new ArrayList<Character>();
		boolean exited = false;
		for(int i = path.length() - 1; i >= 0 && !exited; i--){
			char c = path.charAt(i);
			if(c != '.'){
				chars.add(0, c);
			}
			else
				exited = true;
		}
		
		String extCad = "";
		for(Character c :chars)
			extCad += c;

		switch(extCad){
			case "doc":
				ext = FileExtension.DOC;
				break;
			case "docx":
				ext = FileExtension.DOCX;
				break;
			case "ppt":
				ext = FileExtension.PPT;
				break;
			case "pptx":
				ext = FileExtension.PPTX;
				break;
			case "rar":
				ext = FileExtension.RAR;
				break;
			case "zip":
				ext = FileExtension.ZIP;
				break;
			case "htm":
			case "html":
				ext = FileExtension.HTML;
				break;
			case "pdf":
				ext = FileExtension.PDF;
				break;
			case "chm":
				ext = FileExtension.CHM;
				break;
			/*case "":
				break;*/
			default:
				ext = FileExtension.DEFAULT;
		}
		
		return ext;
	}
	
	public static void main(String[] args){
		String name = "maria";
		String md5 = getMD5(name);
		System.out.println(name + " " + md5);
	}
	
	public static String getMD5(String chain){
		MessageDigest md5;
		String retorno = "";
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(chain.getBytes());
			byte[] keys = md5.digest();
			retorno = new String(new BASE64Encoder().encode(keys));
		} catch (NoSuchAlgorithmException e) {
			DialogBox diag = new DialogBox("Ha ocurrido un error en la codificación de su contraseña");
			diag.show();
		}
		return retorno;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static List<MaterialWithProperty> findCoincidenceAndSort(List<MaterialWithProperty> list, String[] word){
		List<MaterialWithProperty> newList = new LinkedList<MaterialWithProperty>();
		List<AuxiliarMaterial> listAux = new LinkedList<AuxiliarMaterial>();
		
		for(MaterialWithProperty mat : list){
			int count = 0;
			String[] palabras = mat.palabrasClavesProperty().get().split(",");
			for(int i = 0; i < palabras.length; i++){
				int j = 0;
				boolean found = false;
				while(j < word.length && !found){
					if(palabras[i].equalsIgnoreCase(word[j])){
						found = true;
						count++;
					}
					j++;
				}
					
			}
			AuxiliarMaterial aux = new AuxiliarMaterial(mat, count);
			listAux.add(aux);
		}
		Comparable[] cmp = new Comparable[listAux.size()];
		cmp = (Comparable[]) listAux.toArray(cmp);
		Sort.mergeSort_R(cmp);
		
		for(int i = 0; i < cmp.length; i++)
			newList.add(((AuxiliarMaterial)cmp[i]).getMaterial());
		
		return newList;
	}
}

class AuxiliarMaterial implements Comparable<AuxiliarMaterial>{
	private MaterialWithProperty mat;
	private int count;
	
	
	public AuxiliarMaterial(MaterialWithProperty mat, int count) {
		super();
		this.mat = mat;
		this.count = count;
	}


	@Override
	public int compareTo(AuxiliarMaterial other) {
		int value = 0;
		if(count < other.getCount())
			value = -1;
		else if(count > other.getCount())
			value = 1;
		return value;
	}
	
	
	public MaterialWithProperty getMaterial(){
		return mat;
	}
	public int getCount(){
		return count;
	}
}