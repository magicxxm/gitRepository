package view.btnOper.oper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.AutoPathView;

import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * Created by Laptop-6 on 2017/11/17.
 */
public abstract class DefaultButtonAction implements ActionListener {
    protected Logger LOG = LoggerFactory.getLogger(DefaultButtonAction.class.getName());
    protected AutoPathView autoPathView;
    protected int robotId;
    protected Pattern intPattern;
    protected Pattern floatPattern;
    public DefaultButtonAction(AutoPathView autoPathView) {
        this.autoPathView = autoPathView;
        init();
    }
    private void init(){
        String rex = "^\\+?[1-9][0-9]*$";
        intPattern = Pattern.compile(rex);
        rex = "[0-9]*(\\\\.?)[0-9]*";
        floatPattern = Pattern.compile(rex);
    }



}
