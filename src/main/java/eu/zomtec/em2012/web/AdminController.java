package eu.zomtec.em2012.web;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.zomtec.em2012.manager.UpdateJob;

@RequestMapping("/admin/**")
@Controller
public class AdminController {
	
	@Autowired
	private UpdateJob updateJob;

    @RequestMapping(value={"/","index"})
    public String index() {
        return "admin/index";
    }
    
    @RequestMapping(value={"/calculate"})
    public String recalculateAll() throws ClientProtocolException, IOException, JSONException, ParseException {
    	updateJob.calculateAllBets();
    	return "admin/index";
    }
    
    @RequestMapping(value={"/update"})
    public String updateAll() throws ClientProtocolException, IOException, JSONException, ParseException {
    	updateJob.updateAllGameDetails();
    	return "admin/index";
    }
}
