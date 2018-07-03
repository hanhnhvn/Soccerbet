package eu.zomtec.em2012.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.zomtec.em2012.domain.GameGroup;

@RequestMapping("/gamegroups")
@Controller
@RooWebScaffold(path = "gamegroups", formBackingObject = GameGroup.class)
public class GameGroupController {
}
