package stickyNote;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import trashCan.TrashNote;
import utility.ComponentMover;

public class NoteManager extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private ArrayList<Note> noteList = new ArrayList<Note>();
	private JLabel title;
	private JMenuItem newANote;
	private JMenuItem refresh;
	private JMenuItem close;
	private JPanel customTitleBar;
	private ArrayList<JLabel> item = new ArrayList<JLabel>();
	private JPanel listPanel;
	private JScrollPane ScrollPane;
	private JDesktopPane desktopPane;
	private String loadDirectory = "/Desktop/PasteIt/NoteManger";
	private boolean editList = true;

	public NoteManager(JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
	}

	public void initFrame() {
		this.setSize(300, 500);
		this.setResizable(false);
		this.setClosable(false);
		this.setMaximizable(false);
		this.setIconifiable(false);
		this.setBorder(null);
		BasicInternalFrameUI basicInternalFrameUI = (BasicInternalFrameUI) this.getUI();
		basicInternalFrameUI.setNorthPane(null);
		this.setVisible(true);
	}

	public void initCustomTitleBar() {
		this.customTitleBar = new JPanel();
		this.title = new JLabel("便利貼列表");
		this.title.setFont(new Font("微軟正黑體", Font.PLAIN, 20));
		this.title.setPreferredSize(new Dimension(150, 20));
		this.customTitleBar.add(this.title);
		this.newANote = new JMenuItem();

		URL path = NoteManager.class.getResource("/images/add.png");
		this.newANote.setIcon(new ImageIcon(path));
		this.newANote.setToolTipText("新增便利貼");
		this.newANote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewNote();
				noteList.get(noteList.size() - 1).setVisible(true);
			}
		});
		this.customTitleBar.add(this.newANote);

		this.refresh = new JMenuItem();
		path = NoteManager.class.getResource("/images/reload.png");
		this.refresh.setIcon(new ImageIcon(path));
		this.refresh.setToolTipText("重新整理");
		this.refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		this.customTitleBar.add(this.refresh);

		this.close = new JMenuItem();
		path = NoteManager.class.getResource("/images/close.png");
		this.close.setIcon(new ImageIcon(path));
		this.close.setToolTipText("關閉視窗");
		this.close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				saveNote();
			}
		});
		this.customTitleBar.add(this.close);

		this.add(this.customTitleBar, BorderLayout.NORTH);
	}

	public void initListPanel() {
		this.listPanel = new JPanel();
		this.initAllItem();
		this.ScrollPane = new JScrollPane();
		this.ScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		this.ScrollPane.setAutoscrolls(true);
		this.ScrollPane.setViewportView(this.listPanel);
		this.add(this.ScrollPane);
	}

	public void initAllItem() {
		// init listPanel
		if (this.noteList.size() < 9)
			this.listPanel.setLayout(new GridLayout(9, 1, 3, 3));
		else
			this.listPanel.setLayout(new GridLayout(this.noteList.size(), 1, 3, 3));
		for (int i = 0; i < this.noteList.size(); i++) {
			this.item.add(new JLabel(this.noteList.get(i).getNoteTitle()));
			this.item.get(i).setPreferredSize(new Dimension(250, 40));
			this.item.get(i).setFont(new Font("微軟正黑體", Font.PLAIN, 20));
			this.item.get(i).setOpaque(true);
			this.setItemColor(this.noteList.get(i).getColor(), i);
			final Note temp = this.noteList.get(i);
			this.item.get(i).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					openNote(noteList.indexOf(temp));
				}

				public void mouseEntered(MouseEvent event) {
					setItemColorBrighter(noteList.get(noteList.indexOf(temp)).getColor(), noteList.indexOf(temp));
				}

				public void mouseExited(MouseEvent event) {
					setItemColor(noteList.get(noteList.indexOf(temp)).getColor(), noteList.indexOf(temp));
				}
			});
			this.listPanel.add(this.item.get(i), 0);
		}
	}

	public void loadNote() {
		File folder = new File(System.getProperty("user.home") + loadDirectory);
		folder.mkdirs();
		File[] file = folder.listFiles();

		// 檢查垃圾桶中便利貼是否存在超過七天
		if (loadDirectory == "/Desktop/PasteIt/TrashCan/TrashNote") {
			checkAfterSevenDayFile(folder, file);
		}

		for (File note : file) {
			if (note.isFile()) {
				try {
					createNote(readFile(note.getPath(), Charset.forName("MS950")));
					// createNote(readFile(note.getPath(),
					// Charset.forName("UTF-8")));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Load Note Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	static private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

	public void createNote(String read) {
		if (read == "") {
			this.noteList.add(new Note(this.desktopPane, this, null, "", "白色", "中", "標題", "內容"));
		} else {
			String[] temp = read.split("\n", 6);
			if (editList == false) { // 為垃圾桶的便利貼
				if (temp[0].length() == 0) {
					Note addNote = new TrashNote(this.desktopPane, this, null, temp[1], temp[2], temp[3], temp[4],
							temp[5]);
					this.noteList.add(addNote);
				} else {
					String[] tags = temp[0].substring(1).split(", ");
					Note addNote = new TrashNote(this.desktopPane, this, tags, temp[1], temp[2], temp[3], temp[4],
							temp[5]);
					this.noteList.add(addNote);
				}
			} else {
				if (temp[0].length() == 0)
					this.noteList
							.add(new Note(this.desktopPane, this, null, temp[1], temp[2], temp[3], temp[4], temp[5]));
				else {
					String[] tags = temp[0].substring(1).split(", ");
					this.noteList
							.add(new Note(this.desktopPane, this, tags, temp[1], temp[2], temp[3], temp[4], temp[5]));
				}
			}
		}
		this.desktopPane.add(this.noteList.get(this.noteList.size() - 1));
	}

	public void addNewNote() {
		// 建立便利貼
		this.createNote("");
		this.noteList.get(this.noteList.size() - 1).setVisible(true);
		// 更新列表
		this.listPanel.removeAll();
		this.item.clear();
		this.initAllItem();
		this.listPanel.revalidate();
		this.listPanel.repaint();
	}

	public void openNote(int index) {
		this.noteList.get(index).setVisible(true);
	}

	public void changeColor(Note note, String color) {
		for (int i = 0; i < this.noteList.size(); i++) {
			if (this.noteList.get(i) == note) {
				this.noteList.get(i).setColor(color);
				this.setItemColor(this.noteList.get(i).getColor(), i);
				return;
			}
		}
	}

	// 檢查垃圾桶中的檔案是否存在超過七天
	public void checkAfterSevenDayFile(File folder, File[] file) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String nowTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String[] fileList = folder.list();
		int index = 0;
		for (File f : file) {
			if (f.isFile())
				index++;
		}
		for (int i = 0; i < index; i++) {
			String fileTime = "";
			String[] fileData = fileList[i].split("_");
			fileTime += fileData[0];
			fileTime += "_";
			fileTime += fileData[1];
			try {
				Date now = df.parse(nowTime);
				Date date = df.parse(fileTime);
				long l = now.getTime() - date.getTime();
				long day = l / (24 * 60 * 60 * 1000);
				if (day >= 7) {
					File deleteFile = new File(
							System.getProperty("user.home") + "/Desktop/PasteIt/TrashCan/TrashNote/" + fileList[i]);
					try {
						if (deleteFile.delete()) {
							// System.out.println(deleteFile.getName() + " is
							// deleted!");
						} else {
							// System.out.println("Delete operation is
							// failed.");
						}
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteNote(Note note) {
		// 把檔案移到垃圾桶檔案夾
		File folder = new File(System.getProperty("user.home") + "/Desktop/PasteIt/TrashCan/TrashNote");
		folder.mkdirs();
		File[] file = folder.listFiles();
		int index = 0;
		for (File f : file) {
			if (f.isFile())
				index++;
		}
		try {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			PrintWriter printWriter = new PrintWriter(
					folder.getPath() + '/' + timeStamp + "_note_trash_" + index + ".txt");
			printWriter.write(note.save() + "\n");
			printWriter.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Save Note Error", JOptionPane.ERROR_MESSAGE);
		}
		// 更新列表
		for (int i = 0; i < this.noteList.size(); i++) {
			if (this.noteList.get(i) == note) {
				this.noteList.remove(i);
				this.listPanel.remove(this.item.get(i));
				this.item.remove(i);
			}
		}
		this.listPanel.revalidate();
		this.listPanel.repaint();
		note.dispose();
		this.saveNote();
	}

	// 手動刪除垃圾桶中檔案
	public void RealyDeleteNote(Note note) {
		int index = 0;
		for (int i = 0; i < this.noteList.size(); i++) {
			if (this.noteList.get(i) == note) {
				this.noteList.remove(i);
				this.listPanel.remove(this.item.get(i));
				this.item.remove(i);
				index = i;
			}
		}
		File folder = new File(System.getProperty("user.home") + "/Desktop/PasteIt/TrashCan/TrashNote");
		String[] fileList = folder.list();
		File deleteFile = new File(
				System.getProperty("user.home") + "/Desktop/PasteIt/TrashCan/TrashNote/" + fileList[index]);
		try {
			if (deleteFile.delete()) {
				// System.out.println(deleteFile.getName() + " is deleted!");
			} else {
				// System.out.println("Delete operation is failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.listPanel.revalidate();
		this.listPanel.repaint();
		note.dispose();
	}// end mothed RealyDeleteNote

	// 把檔案從垃圾桶還原到原處
	public void recoverNote(Note note) {
		File folder = new File(System.getProperty("user.home") + "/Desktop/PasteIt/NoteManger");
		folder.mkdirs();
		File[] file = folder.listFiles();
		int index = 0;
		for (File f : file) {
			if (f.isFile())
				index++;
		}
		try {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			PrintWriter printWriter = new PrintWriter(folder.getPath() + '/' + timeStamp + "_" + index + ".txt");
			printWriter.write(note.save() + "\n");
			printWriter.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Save Note Error", JOptionPane.ERROR_MESSAGE);
		}
		RealyDeleteNote(note);
	} // end mothed recoverNote

	public void closeNote(Note note) {
		// 更新列表
		for (int i = 0; i < this.noteList.size(); i++) {
			if (this.noteList.get(i) == note) {
				this.item.get(i).setText(note.getNoteTitle());
			}
		}
		this.saveNote();
		note.setVisible(false);
	}

	public void saveNote() {
		File folder = new File(System.getProperty("user.home") + "/Desktop/PasteIt/NoteManger");
		folder.mkdirs();
		File[] file = folder.listFiles();
		// 刪除原本所有的note
		for (File note : file) {
			if (note.isFile())
				note.delete();
		}
		// 儲存所有的note
		int i = 0;
		for (Note note : this.noteList) {
			try {
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				PrintWriter printWriter = new PrintWriter(folder.getPath() + '/' + timeStamp + "_" + i++ + ".txt");
				printWriter.write(note.save() + "\n");
				printWriter.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Save Note Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void setItemColor(String color, int i) {
		switch (color) {
		case "白色":
			this.item.get(i).setBackground(new Color(225, 225, 225));
			break;
		case "粉紅色":
			this.item.get(i).setBackground(new Color(255, 153, 153));
			break;
		case "黃色":
			this.item.get(i).setBackground(new Color(255, 255, 148));
			break;
		case "綠色":
			this.item.get(i).setBackground(new Color(204, 255, 128));
			break;
		case "藍色":
			this.item.get(i).setBackground(new Color(153, 204, 255));
			break;
		case "紫色":
			this.item.get(i).setBackground(new Color(203, 143, 255));
			break;
		default:
			this.item.get(i).setBackground(new Color(225, 225, 225));
			break;
		}
	}

	public void setItemColorBrighter(String color, int i) {
		switch (color) {
		case "白色":
			this.item.get(i).setBackground(new Color(225, 225, 225).brighter());
			break;
		case "粉紅色":
			this.item.get(i).setBackground(new Color(255, 153, 153).brighter());
			break;
		case "黃色":
			this.item.get(i).setBackground(new Color(255, 255, 148).brighter());
			break;
		case "綠色":
			this.item.get(i).setBackground(new Color(204, 255, 128).brighter());
			break;
		case "藍色":
			this.item.get(i).setBackground(new Color(153, 204, 255).brighter());
			break;
		case "紫色":
			this.item.get(i).setBackground(new Color(203, 143, 255).brighter());
			break;
		default:
			this.item.get(i).setBackground(new Color(225, 225, 225).brighter());
			break;
		}
	}

	public void refresh() {
		this.noteList.clear();
		this.loadNote();
		// 更新列表
		this.listPanel.removeAll();
		this.item.clear();
		this.initAllItem();
		this.listPanel.revalidate();
		this.listPanel.repaint();
	}

	// 設定讀檔路徑
	public void setLoadDirectory(String newDirectory) {
		loadDirectory = newDirectory;
	}

	// 設定判斷是否改為不可編輯的參數
	public void setEditList(boolean b) {
		editList = b;
	}

	// 取得listPanel
	public JPanel getListPanel() {
		return this.listPanel;
	}

	// 取得noteList大小
	public int getNoteListSize() {
		return this.noteList.size();
	}

	public void run() {
		java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.loadNote();
		this.initCustomTitleBar();
		this.initListPanel();
		this.initFrame();
		setLocation((scr_size.width - this.getWidth()) / 2, (scr_size.height - this.getHeight()) / 2);
		@SuppressWarnings("unused")
		ComponentMover componentMover1 = new ComponentMover(JInternalFrame.class, this.customTitleBar);
		@SuppressWarnings("unused")
		ComponentMover componentMover2 = new ComponentMover(JInternalFrame.class, this.listPanel);
	}
}