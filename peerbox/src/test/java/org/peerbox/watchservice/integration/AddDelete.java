package org.peerbox.watchservice.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;
import org.peerbox.utils.FileTestUtils;

public class AddDelete extends FileIntegrationTest {
	
//	private static final int NUMBER_OF_CHARS = 1000*100; // approx. 100kb
//	private static final int WAIT_TIME_SHORT = 30;
//	private static final int WAIT_TIME_LONG = 5*60;
	
	@Test
	public void singleFolderTest() throws IOException {
		// ADD
		Path folder = addSingleFolder();
		
		// DELETE
		deleteSingleFolder(folder);
	}
	
	private void deleteSingleFolder(Path folder) throws IOException {
		Files.delete(folder);
		
		waitForNotExists(folder, WAIT_TIME_SHORT);
		assertSyncClientPaths();
	}
	
	@Test
	public void manyFoldersTest() throws IOException {
		// ADD
		List<Path> folders = addManyFolders();
		
		// DELETE
		deleteManyFolders(folders);
	}

	private List<Path> addManyFolders() throws IOException {
		int numFolders = 20;
		List<Path> folders = FileTestUtils.createRandomFolders(client.getRootPath(), numFolders);
	
		waitForExists(folders, WAIT_TIME_LONG);
		assertSyncClientPaths();
		return folders;
	}
	
	private void deleteManyFolders(List<Path> folders) throws IOException {
		for(Path f : folders) {
			Files.delete(f);
		}
		
		waitForNotExists(folders, WAIT_TIME_LONG);
		assertSyncClientPaths();
	}
	
	@Test
	public void singleFolderInFolderTest() throws IOException {
		// ADD
		List<Path> folders = addSingleFolderInFolder();
		
		// DELETE
		deleteSingleFolderInFolder(folders);
	}
	
	private List<Path> addSingleFolderInFolder() throws IOException {
		Path folder = FileTestUtils.createRandomFolder(client.getRootPath());
		Path subFolder = FileTestUtils.createRandomFolder(folder);
		
		List<Path> folders = new ArrayList<>();
		folders.add(folder);
		folders.add(subFolder);
		
		waitForExists(folders, WAIT_TIME_LONG);
		assertSyncClientPaths();
		return folders;
	}
	
	private void deleteSingleFolderInFolder(List<Path> folders) throws IOException {
		ListIterator<Path> it = folders.listIterator(folders.size());
		while(it.hasPrevious()) {
			Path f = it.previous();
			Files.delete(f);
		}
		
		waitForNotExists(folders, WAIT_TIME_LONG);
		assertSyncClientPaths();
	}
	
	@Test
	public void manyFoldersInFolderTest() throws IOException {
		// ADD
		List<Path> folders = addManyFoldersInFolder();
		
		// DELETE
		deleteManyFoldersInFolder(folders);
	}
	
	private List<Path> addManyFoldersInFolder() throws IOException {
		List<Path> folders = new ArrayList<>();
		
		Path folder = FileTestUtils.createRandomFolder(client.getRootPath());
		List<Path> subFolders = FileTestUtils.createRandomFolders(folder, 20);
		
		folders.add(folder);
		folders.addAll(subFolders);
		
		waitForExists(folders, WAIT_TIME_LONG);
		assertSyncClientPaths();
		return folders;
	}
	
	private void deleteManyFoldersInFolder(List<Path> folders) throws IOException {
		ListIterator<Path> it = folders.listIterator(folders.size());
		while(it.hasPrevious()) {
			Path f = it.previous();
			Files.delete(f);
		}
		
		waitForNotExists(folders, WAIT_TIME_LONG);
		assertSyncClientPaths();
	}
	
	@Test
	public void singleFileTest() throws IOException {
		// ADD
		Path file = addSingleFile();
		
		// DELETE
		deleteSingleFile(file);
	}
	
	private Path addSingleFile() throws IOException {
		Path file = FileTestUtils.createRandomFile(client.getRootPath(), NUMBER_OF_CHARS);
		
		waitForExists(file, WAIT_TIME_SHORT);
		assertSyncClientPaths();
		return file;
	}
	
	private void deleteSingleFile(Path file) throws IOException {
		Files.delete(file);
		
		waitForNotExists(file, WAIT_TIME_SHORT);
		assertSyncClientPaths();
	}
	
	@Test
	public void manyFilesTest() throws IOException {
		List<Path> files = addManyFiles();
		
		deleteManyFiles(files);
	}
	
	private void deleteManyFiles(List<Path> files) throws IOException {
		for(Path f : files) {
			Files.delete(f);
		}
		
		waitForNotExists(files, WAIT_TIME_LONG);
		assertSyncClientPaths();
	}
	
	@Test
	public void singleFileInFolderTest() throws IOException {
		// ADD
		List<Path> files = addSingleFileInFolder();
		
		// DELETE
		deleteSingleFileInFolder(files);
	}
	
	private List<Path> addSingleFileInFolder() throws IOException {
		List<Path> files = FileTestUtils.createFolderWithFiles(client.getRootPath(), 1, NUMBER_OF_CHARS);
		
		waitForExists(files, WAIT_TIME_SHORT);
		assertSyncClientPaths();
		return files;
	}
	
	private void deleteSingleFileInFolder(List<Path> files) throws IOException {
		ListIterator<Path> it = files.listIterator(files.size());
		while(it.hasPrevious()) {
			Path f = it.previous();
			Files.delete(f);
		}
		
		waitForNotExists(files, WAIT_TIME_SHORT);
		assertSyncClientPaths();
	}

	@Test
	public void manyFilesInFolderTest() throws IOException {
		// ADD
		List<Path> files = addManyFilesInFolder();
		
		// DELETE
		deleteManyFilesInFolder(files);
	}
	
	private List<Path> addManyFilesInFolder() throws IOException {
		List<Path> files = FileTestUtils.createFolderWithFiles(client.getRootPath(), 100, NUMBER_OF_CHARS);
		
		waitForExists(files, WAIT_TIME_LONG);
		assertSyncClientPaths();
		return files;
	}
	
	private void deleteManyFilesInFolder(List<Path> files) throws IOException {
		ListIterator<Path> it = files.listIterator(files.size());
		while(it.hasPrevious()) {
			Path p = it.previous();
			Files.delete(p);
		}
		
		waitForNotExists(files, WAIT_TIME_LONG);
		assertSyncClientPaths();
	}

	@Test
	public void manyFilesInManyFoldersTest() throws IOException {
		// ADD
		List<Path> files = addManyFilesInManyFolders();
		
		// DELETE
		deleteManyFilesInManyFolders(files);
	}
	
	private List<Path> addManyFilesInManyFolders() throws IOException {
		List<Path> files = new ArrayList<>();
		int numFolders = 20;
		int numFilesPerFolder = 20;
		for(int i = 0; i < numFolders; ++i) {
			List<Path> f = FileTestUtils.createFolderWithFiles(client.getRootPath(), numFilesPerFolder, NUMBER_OF_CHARS);
			files.addAll(f);
		}
		
		waitForExists(files, WAIT_TIME_LONG);		
		assertSyncClientPaths();
		return files;
	}

	private void deleteManyFilesInManyFolders(List<Path> files) throws IOException {
		List<Path> folders = new ArrayList<Path>();
		// delete files
		for(Path p : files) {
			if(Files.isDirectory(p)) {
				folders.add(p);
				continue;
			}
			Files.delete(p);
		}
		
		// delete (now empty) folders
		for(Path p : folders) {
			Files.delete(p);
		}
		
		waitForNotExists(files, WAIT_TIME_LONG);
		assertSyncClientPaths();
	}
}