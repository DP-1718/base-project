
package utilities;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ViewManager {

	private static final String ROOT_PATH = Paths.get("").toAbsolutePath().toString();
	private static final String VIEWS_PATH = "/src/main/webapp/views";
	private static final String RESOURCES_PATH = "/src/main/resources";

	public static void main(final String[] args) {
		Collection<File> viewFoldersPaths;

		System.out.println("Starting ViewManager \n------------------------------------\n");

		System.out.print("Deleting old tiles files    .....");
		ViewManager.purgeOldTilesFiles();
		System.out.println(" OK");

		System.out.print("Deleting old messages files .....");
		ViewManager.purgeOldMessagesFiles();
		System.out.println(" OK");

		System.out.println("\n------------------------------------\n");

		viewFoldersPaths = ViewManager.getViewFoldersPaths();

		System.out.print("Adding new tiles files      .....");
		ViewManager.addTilesFilesToConfiguration(viewFoldersPaths);
		System.out.println(" OK");

		System.out.print("Adding new messages files   .....");
		ViewManager.addMessagesFilesToConfiguration(viewFoldersPaths);
		System.out.println(" OK");

	}

	private static Collection<File> getViewFoldersPaths() {
		Collection<File> viewFoldersPaths;
		Path viewsPath;

		viewFoldersPaths = new ArrayList<File>();
		viewsPath = Paths.get((ViewManager.ROOT_PATH + ViewManager.VIEWS_PATH).replace("/", "\\"));

		if (viewsPath.toFile().isDirectory()) {
			for (File viewFolder : viewsPath.toFile().listFiles()) {
				if (viewFolder.isDirectory()) {
					viewFoldersPaths.add(viewFolder);
				}
			}
		}

		return viewFoldersPaths;
	}

	private static Boolean addTilesFilesToConfiguration(Collection<File> viewFoldersPaths) {
		Boolean result;
		DocumentBuilderFactory documentFactory;
		DocumentBuilder documentBuilder;
		Document document;
		Node container;
		Element fileToAdd;

		result = true;

		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
			document = documentBuilder
					.parse(ViewManager.ROOT_PATH + ViewManager.RESOURCES_PATH + "/spring/config/tiles.xml");
			container = document.getElementsByTagName("util:list").item(0);

			for (File viewFolder : viewFoldersPaths) {
				if (Paths.get(viewFolder.getAbsolutePath() + "/tiles.xml").toFile().exists()) {
					fileToAdd = document.createElement("value");
					fileToAdd.setTextContent("/views/" + viewFolder.getName() + "/tiles.xml");
					container.appendChild(fileToAdd);
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(document);
			StreamResult xmlResult = new StreamResult(
					new File(ViewManager.ROOT_PATH + ViewManager.RESOURCES_PATH + "/spring/config/tiles.xml"));
			transformer.transform(source, xmlResult);

		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}

	private static Boolean addMessagesFilesToConfiguration(Collection<File> viewFoldersPaths) {
		Boolean result;
		DocumentBuilderFactory documentFactory;
		DocumentBuilder documentBuilder;
		Document document;
		Node container;
		Element fileToAdd;

		result = true;

		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
			document = documentBuilder
					.parse(ViewManager.ROOT_PATH + ViewManager.RESOURCES_PATH + "/spring/config/i18n-l10n.xml");
			container = document.getElementsByTagName("util:list").item(0);

			for (File viewFolder : viewFoldersPaths) {
				if (Paths.get(viewFolder.getAbsolutePath() + "/messages.properties").toFile().exists()) {
					fileToAdd = document.createElement("value");
					fileToAdd.setTextContent("/views/" + viewFolder.getName() + "/messages");
					container.appendChild(fileToAdd);
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(document);
			StreamResult xmlResult = new StreamResult(
					new File(ViewManager.ROOT_PATH + ViewManager.RESOURCES_PATH + "/spring/config/i18n-l10n.xml"));
			transformer.transform(source, xmlResult);

		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}

	private static Boolean purgeOldTilesFiles() {

		Boolean result;
		DocumentBuilderFactory documentFactory;
		DocumentBuilder documentBuilder;
		Document document;
		Collection<Node> nodesToRemove;
		Node container;
		NodeList files;

		result = true;
		nodesToRemove = new ArrayList<Node>();

		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
			document = documentBuilder
					.parse(ViewManager.ROOT_PATH + ViewManager.RESOURCES_PATH + "/spring/config/tiles.xml");
			container = document.getElementsByTagName("util:list").item(0);
			files = container.getChildNodes();

			for (int i = 0; i < files.getLength(); i++) {
				files.item(i).setTextContent(null);
				nodesToRemove.add(files.item(i));
			}

			for (Node node : nodesToRemove) {
				try {
					container.removeChild(node);
				} catch (org.w3c.dom.DOMException e) {
					result = false;
					e.printStackTrace();
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(document);
			StreamResult xmlResult = new StreamResult(
					new File(ViewManager.ROOT_PATH + ViewManager.RESOURCES_PATH + "/spring/config/tiles.xml"));
			transformer.transform(source, xmlResult);

		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}

	private static Boolean purgeOldMessagesFiles() {

		Boolean result;
		DocumentBuilderFactory documentFactory;
		DocumentBuilder documentBuilder;
		Document document;
		Collection<Node> nodesToRemove;
		Node container;
		NodeList files;

		result = true;
		nodesToRemove = new ArrayList<Node>();

		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
			document = documentBuilder
					.parse(ViewManager.ROOT_PATH + ViewManager.RESOURCES_PATH + "/spring/config/i18n-l10n.xml");
			container = document.getElementsByTagName("util:list").item(0);
			files = container.getChildNodes();

			for (int i = 0; i < files.getLength(); i++) {
				files.item(i).setTextContent(null);
				nodesToRemove.add(files.item(i));
			}

			for (Node node : nodesToRemove) {
				try {
					container.removeChild(node);
				} catch (org.w3c.dom.DOMException e) {
					result = false;
					e.printStackTrace();
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(document);
			StreamResult xmlResult = new StreamResult(
					new File(ViewManager.ROOT_PATH + ViewManager.RESOURCES_PATH + "/spring/config/i18n-l10n.xml"));
			transformer.transform(source, xmlResult);

		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}

}
