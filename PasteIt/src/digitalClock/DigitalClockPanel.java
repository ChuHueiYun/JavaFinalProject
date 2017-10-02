package digitalClock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

public class DigitalClockPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel dateLabel;
	private JLabel clockLabel;
	private DateFormat timeFormat;
	private DateFormat dateFormat;
	private Timer timer;
	private long lastSeconds;

	public DigitalClockPanel() {
		setLayout(new BorderLayout());

		clockLabel = new JLabel();
		clockLabel.setOpaque(true);
		clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(clockLabel, BorderLayout.CENTER);
		
		dateLabel = new JLabel();
		dateLabel.setOpaque(true);
		dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(dateLabel, BorderLayout.NORTH);
		dateLabel.setPreferredSize(new Dimension(60, 25));
		// Sets the default time format based on current locale.
		
		setDateFormat("Asia/Taipei");	//預設時間格式

		// Setups a timer which fires events every 1/10 of second (100 milliseconds).
		timer = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateClock();
			}
		});

		// Starts the timer.
		timer.start();
	}

	public DateFormat getDateFormat() {
		return timeFormat;
	}

	public void setDateFormat(String timeZone) {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));	//設定時區
		this.timeFormat = new SimpleDateFormat("HH:mm:ss");
		this.dateFormat = new SimpleDateFormat("MM/dd");
		updateClock();
	}

	public void setClockBorder(Border border) {
		clockLabel.setBorder(border);
		dateLabel.setBorder(border);
	}

	public void setClockBackground(Color background) {
		clockLabel.setBackground(background);
		dateLabel.setBackground(background);
	}

	public void setClockForeground(Color foreground) {
		clockLabel.setForeground(foreground);
		dateLabel.setForeground(foreground);
	}

	public void setClockFont(Font clockFont, Font dateFont) {
		clockLabel.setFont(clockFont);
		dateLabel.setFont(dateFont);
	}

	private void updateClock() {
		Date currentDate = new Date();
		long millis = currentDate.getTime();
		long seconds = millis / 1000;

		if (lastSeconds != seconds) {
			lastSeconds = seconds;

			String clockText = timeFormat.format(currentDate);
			clockLabel.setText(clockText);
			String dateText  = dateFormat.format(currentDate);
			dateLabel.setText(dateText);
			
		}
	}
}