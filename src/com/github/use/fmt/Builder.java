package com.github.use.fmt;

import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.JavaPlugin;

@SuppressWarnings("restriction")
public class Builder extends IncrementalProjectBuilder {

    public static final String ARG_KEY = "formatter";
    public static final String PREFERENCE_KEY = "formatter_profile";
    public static final String DEFAULT_FORMATTER = "org.eclipse.jdt.ui.default.eclipse_profile";
    
    static Logger logger = Logger.getLogger(Builder.class.getName());

    public Builder() {
    }

    @SuppressWarnings( { "unchecked" })
    @Override
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
        logger.info(String.format("current formatter is %s", getFormatter()));
        if (args.isEmpty() || !args.containsKey(ARG_KEY)) {
            return null;
        }

        if (getFormatter().equals(trimToEmpty(args.get(ARG_KEY)))) {
            return null;
        }        

        setFormatter(trimToEmpty(args.get(ARG_KEY)));
        logger.info(String.format("current formatter is %s", getFormatter()));
        return null;
    }
    
    private String getFormatter(){
        return JavaPlugin.getDefault().getPreferenceStore().getString(PREFERENCE_KEY);
    }
    
    private void setFormatter(String fmt) {
        logger.info(String.format(
                "try to change the formatter from [%s] to [%s]", 
                getFormatter(), trimToEmpty(fmt)));
        
        JavaPlugin.getDefault().getPreferenceStore().setValue(PREFERENCE_KEY, trimToEmpty(fmt));
    }

    private String trimToEmpty(Object value) {
        if (value == null) {
            return DEFAULT_FORMATTER;
        }
        String fmt = "" + value;
        return fmt.trim();
    }

}
