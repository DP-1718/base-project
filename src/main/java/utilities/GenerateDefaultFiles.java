
package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

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

public class GenerateDefaultFiles {

	private static final Charset CHARSET = StandardCharsets.UTF_8;
	private static final String ROOT_PATH = Paths.get("").toAbsolutePath().toString();
	private static final String CODE_PATH = "/src/main/java";
	private static final String RESOURCES_PATH = "/src/main/resources";
	private static final String DEFAULT_FILES_PATH = GenerateDefaultFiles.CODE_PATH + "/utilities/defaultFiles";
	private static final String XML_BLANK_LINE = "\n";
	private static final String XML_INDENTED_BLANK_LINE = "\n		";

	public static void main(final String[] args) {
		Collection<String> classNames;
		Boolean removeFiles, addFiles;

		System.out.println("Reading class names from file: ");

		classNames = GenerateDefaultFiles.readClassNamesFromFiles();

		for (String className : classNames) {

			addFiles = (!className.startsWith("-") && !className.startsWith("+")) || (className.startsWith("+"));
			removeFiles = (className.startsWith("-"));

			className = (className.startsWith("-") || className.startsWith("+")) ? className.substring(1) : className;

			if (addFiles) {
				System.out.print("Generating files for class " + className);
			} else if (removeFiles) {
				System.out.print("Removing files for class " + className);
			}

			try {

				Class.forName("domain." + className);
				System.out.print(" .");

				if (addFiles) {
					GenerateDefaultFiles.generateRepository(className);
					System.out.print(".");
					GenerateDefaultFiles.generateService(className);
					System.out.print(".");
					GenerateDefaultFiles.generateConverterToString(className);
					System.out.print(".");
					GenerateDefaultFiles.generateConverterToObject(className);
					System.out.print(". ");
				} else if (removeFiles) {
					GenerateDefaultFiles.deleteRepository(className);
					System.out.print(".");
					GenerateDefaultFiles.deleteService(className);
					System.out.print(".");
					GenerateDefaultFiles.deleteConverterToString(className);
					System.out.print(".");
					GenerateDefaultFiles.deleteConverterToObject(className);
					System.out.print(".");
				}
				System.out.println(" OK");

			} catch (final ClassNotFoundException e) {
				System.out.println("Class not found: " + className);
			}
		}
		GenerateDefaultFiles.addConvertersToConfiguration(classNames);

	}

	private static Collection<String> readClassNamesFromFiles() {
		Collection<String> classNames;
		Scanner scanner;

		classNames = new ArrayList<String>();
		try {
			scanner = new Scanner(new File(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.DEFAULT_FILES_PATH
					+ "/class.names".replace("/", "\\")));

			while (scanner.hasNext()) {
				classNames.add(scanner.next());
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return classNames;
	}

	private static Boolean generateRepository(String className) {
		Path defaultRepositoryPath, repositoryPath;
		String content;
		Boolean result;

		result = true;
		defaultRepositoryPath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.DEFAULT_FILES_PATH
				+ "/repository.default".replace("/", "\\"));
		repositoryPath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.CODE_PATH + "/repositories/"
				+ className + "Repository.java".replace("/", "\\"));

		try {
			if (!repositoryPath.toFile().exists()) {
				content = new String(Files.readAllBytes(defaultRepositoryPath), GenerateDefaultFiles.CHARSET);
				content = content.replaceAll("xxxxx", className);
				Files.write(repositoryPath, content.getBytes(GenerateDefaultFiles.CHARSET));
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private static Boolean deleteRepository(String className) {
		Boolean result;
		Path repositoryPath;

		result = true;
		repositoryPath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.CODE_PATH + "/repositories/"
				+ className + "Repository.java".replace("/", "\\"));

		try {
			result = Files.deleteIfExists(repositoryPath);
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private static Boolean generateService(String className) {
		Path defaultServicePath, servicePath;
		String content;
		Boolean result;

		result = true;
		defaultServicePath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.DEFAULT_FILES_PATH
				+ "/service.default".replace("/", "\\"));
		servicePath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.CODE_PATH + "/services/"
				+ className + "Service.java".replace("/", "\\"));

		try {
			if (!servicePath.toFile().exists()) {
				content = new String(Files.readAllBytes(defaultServicePath), GenerateDefaultFiles.CHARSET);
				content = content.replaceAll("xxxxx", className);
				content = content.replaceAll("yyyyy", className.toLowerCase());
				Files.write(servicePath, content.getBytes(GenerateDefaultFiles.CHARSET));
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private static Boolean deleteService(String className) {
		Boolean result;
		Path servicePath;

		result = true;
		servicePath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.CODE_PATH + "/services/"
				+ className + "Service.java".replace("/", "\\"));

		try {
			result = Files.deleteIfExists(servicePath);
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private static Boolean generateConverterToString(String className) {
		Path defaultConverterToStringPath, converterToStringPath;
		String content;
		Boolean result;

		result = true;
		defaultConverterToStringPath = Paths.get(GenerateDefaultFiles.ROOT_PATH
				+ GenerateDefaultFiles.DEFAULT_FILES_PATH + "/converterToString.default".replace("/", "\\"));
		converterToStringPath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.CODE_PATH
				+ "/converters/" + className + "ToStringConverter.java".replace("/", "\\"));

		try {
			content = new String(Files.readAllBytes(defaultConverterToStringPath), GenerateDefaultFiles.CHARSET);
			content = content.replaceAll("xxxxx", className);
			content = content.replaceAll("yyyyy", className.toLowerCase());
			Files.write(converterToStringPath, content.getBytes(GenerateDefaultFiles.CHARSET));
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private static Boolean deleteConverterToString(String className) {
		Boolean result;
		Path converterToStringPath;

		result = true;
		converterToStringPath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.CODE_PATH
				+ "/converters/" + className + "ToStringConverter.java".replace("/", "\\"));

		try {
			result = Files.deleteIfExists(converterToStringPath);
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private static Boolean generateConverterToObject(String className) {
		Path defaultConverterToObjectPath, converterToObjectPath;
		String content;
		Boolean result;

		result = true;
		defaultConverterToObjectPath = Paths.get(GenerateDefaultFiles.ROOT_PATH
				+ GenerateDefaultFiles.DEFAULT_FILES_PATH + "/converterToObject.default".replace("/", "\\"));
		converterToObjectPath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.CODE_PATH
				+ "/converters/StringTo" + className + "Converter.java".replace("/", "\\"));

		try {
			content = new String(Files.readAllBytes(defaultConverterToObjectPath), GenerateDefaultFiles.CHARSET);
			content = content.replaceAll("xxxxx", className);
			content = content.replaceAll("yyyyy", className.toLowerCase());
			Files.write(converterToObjectPath, content.getBytes(GenerateDefaultFiles.CHARSET));
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private static Boolean deleteConverterToObject(String className) {
		Boolean result;
		Path converterToObjectPath;

		result = true;
		converterToObjectPath = Paths.get(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.CODE_PATH
				+ "/converters/StringTo" + className + "Converter.java".replace("/", "\\"));

		try {
			result = Files.deleteIfExists(converterToObjectPath);
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private static Boolean addConvertersToConfiguration(Collection<String> classNames) {
		Boolean result, purgeResult;
		DocumentBuilderFactory documentFactory;
		DocumentBuilder documentBuilder;
		Document document;

		result = true;

		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
			document = documentBuilder.parse(GenerateDefaultFiles.ROOT_PATH + GenerateDefaultFiles.RESOURCES_PATH
					+ "/spring/config/converters.xml");

			purgeResult = GenerateDefaultFiles.purgeOldConverters(document);

			if (purgeResult) {
				result = GenerateDefaultFiles.insertConverters(classNames, document);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(document);
			StreamResult xmlResult = new StreamResult(new File(GenerateDefaultFiles.ROOT_PATH
					+ GenerateDefaultFiles.RESOURCES_PATH + "/spring/config/converters.xml"));
			transformer.transform(source, xmlResult);

		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}

	private static Boolean insertConverters(Collection<String> classNames, Document document) {
		Boolean result, classToAdd;
		Node convertersContainer;
		Element converterToObject;
		Element converterToString;

		result = true;
		convertersContainer = document.getElementsByTagName("util:list").item(0);

		for (String className : classNames) {

			classToAdd = (!className.startsWith("-") && !className.startsWith("+")) || (className.startsWith("+"));

			if (classToAdd) {
				className = (className.startsWith("-") || className.startsWith("+")) ? className.substring(1)
						: className;

				convertersContainer.appendChild(document.createTextNode(GenerateDefaultFiles.XML_BLANK_LINE));
				convertersContainer.appendChild(document.createTextNode(GenerateDefaultFiles.XML_INDENTED_BLANK_LINE));
				convertersContainer.appendChild(document.createComment(className + " converters"));
				convertersContainer.appendChild(document.createTextNode(GenerateDefaultFiles.XML_INDENTED_BLANK_LINE));

				converterToObject = document.createElement("bean");
				converterToObject.setAttribute("class", "converters.StringTo" + className + "Converter");
				convertersContainer.appendChild(converterToObject);

				converterToString = document.createElement("bean");
				converterToString.setAttribute("class", "converters." + className + "ToStringConverter");
				convertersContainer.appendChild(converterToString);
			}
		}

		return result;
	}

	private static Boolean purgeOldConverters(Document document) {
		Boolean result, startPurge;
		Collection<Node> nodesToRemove;
		Node convertersContainer;
		NodeList converters;

		result = true;
		startPurge = false;
		nodesToRemove = new ArrayList<Node>();
		convertersContainer = document.getElementsByTagName("util:list").item(0);
		converters = convertersContainer.getChildNodes();

		for (int i = 0; i < converters.getLength(); i++) {
			if (startPurge) {
				converters.item(i).setTextContent(null);
				nodesToRemove.add(converters.item(i));
			} else if (converters.item(i).getNodeName().equals("#comment")
					&& converters.item(i).getTextContent().equals("Autogenerated converters")) {
				startPurge = true;
			}
		}

		for (Node node : nodesToRemove) {
			try {
				convertersContainer.removeChild(node);
			} catch (org.w3c.dom.DOMException e) {
				result = false;
				e.printStackTrace();
			}
		}

		result = result && startPurge;

		return result;
	}

}
