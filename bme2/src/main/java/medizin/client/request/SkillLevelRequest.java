package medizin.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import medizin.client.proxy.SkillLevelProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

@RooGwtUnmanagedRequest("medizin.server.domain.SkillLevel")
@ServiceName("medizin.server.domain.SkillLevel")
public interface SkillLevelRequest extends SkillLevelRequest_Roo_Gwt {
	
	Request<List<SkillLevelProxy>> findAllSkillLevelsByLevelASC();
}

