package us.cyrien.mcutils.dispatcher.help;

import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;

import java.util.List;

public class IndexTopic extends IndexHelpTopic {
	public IndexTopic(String name, List<HelpTopic> topics) {
		super(name, "HelpCmd for " + name, "", topics);
	}
}
