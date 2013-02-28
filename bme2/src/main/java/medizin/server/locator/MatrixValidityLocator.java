package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.MatrixValidity;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.MatrixValidity")
@Component
public class MatrixValidityLocator extends Locator<MatrixValidity, Long> {

    public MatrixValidity create(Class<? extends medizin.server.domain.MatrixValidity> clazz) {
        return new MatrixValidity();
    }

    public MatrixValidity find(Class<? extends medizin.server.domain.MatrixValidity> clazz, Long id) {
        return MatrixValidity.findMatrixValidity(id);
    }

    public Class<medizin.server.domain.MatrixValidity> getDomainType() {
        return MatrixValidity.class;
    }

    public Long getId(MatrixValidity matrixValidity) {
        return matrixValidity.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(MatrixValidity matrixValidity) {
        return matrixValidity.getVersion();
    }
}
