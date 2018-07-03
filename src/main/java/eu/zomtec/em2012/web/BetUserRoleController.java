package eu.zomtec.em2012.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.zomtec.em2012.domain.BetUserRole;

@RequestMapping("/betuserroles")
@Controller
@RooWebScaffold(path = "betuserroles", formBackingObject = BetUserRole.class)
public class BetUserRoleController {
}
