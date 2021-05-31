package net.minecraft.command;

public class EntityNotFoundException extends CommandException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String __OBFID = "CL_00002335";

    public EntityNotFoundException() {
        this("commands.generic.entity.notFound", new Object[0]);
    }

    public EntityNotFoundException(String p_i46035_1_, Object... p_i46035_2_) {
        super(p_i46035_1_, p_i46035_2_);
    }
}
