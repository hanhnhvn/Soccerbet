package eu.zomtec.em2012.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.zomtec.em2012.domain.BetUser;

@RequestMapping("/betusers")
@Controller
@RooWebScaffold(path = "betusers", formBackingObject = BetUser.class)
public class BetUserController {
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid BetUser betUser, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, betUser);
            return "betusers/update";
        }
        
        hashPassword(betUser);
        
        uiModel.asMap().clear();
        betUser.merge();
        return "redirect:/betusers/" + encodeUrlPathSegment(betUser.getId().toString(), httpServletRequest);
    }

	private void hashPassword(BetUser betUser) {
		if (StringUtils.length(betUser.getPassword()) < 32) {
        	betUser.setPassword(DigestUtils.sha256Hex(betUser.getPassword()));
        }
	}
}
