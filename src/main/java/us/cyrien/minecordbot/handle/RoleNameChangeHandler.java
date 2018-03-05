package us.cyrien.minecordbot.handle;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import us.cyrien.minecordbot.Minecordbot;

import java.util.Set;

public class RoleNameChangeHandler extends ListenerAdapter {

    private Minecordbot mcb;

    public RoleNameChangeHandler(Minecordbot mcb) {
        this.mcb = mcb;
    }

    @Override
    public void onRoleUpdateName(RoleUpdateNameEvent event) {
        super.onRoleUpdateName(event);
        if (exists(event.getRole())) {
            mcb.getMcbConfigsManager().getPermConfig().getConfig().set(event.getRole().getId() + ".RoleName", event.getRole().getName());
            mcb.getMcbConfigsManager().getPermConfig().getConfig().saveConfig();
            mcb.getMcbConfigsManager().reloadAllConfig();
        }
    }

    private boolean exists(Role role) {
        Set<String> keys = mcb.getMcbConfigsManager().getPermConfig().getConfig().getKeys();
        return keys.contains(role.getId());
    }
}
