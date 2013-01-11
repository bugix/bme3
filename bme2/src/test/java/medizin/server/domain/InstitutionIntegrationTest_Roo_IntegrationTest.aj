// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.util.List;
import medizin.server.domain.Institution;
import medizin.server.domain.InstitutionDataOnDemand;
import medizin.server.domain.InstitutionIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect InstitutionIntegrationTest_Roo_IntegrationTest {
    
    declare @type: InstitutionIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: InstitutionIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: InstitutionIntegrationTest: @Transactional;
    
    @Autowired
    InstitutionDataOnDemand InstitutionIntegrationTest.dod;
    
    @Test
    public void InstitutionIntegrationTest.testCountInstitutions() {
        Assert.assertNotNull("Data on demand for 'Institution' failed to initialize correctly", dod.getRandomInstitution());
        long count = Institution.countInstitutions();
        Assert.assertTrue("Counter for 'Institution' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void InstitutionIntegrationTest.testFindInstitution() {
        Institution obj = dod.getRandomInstitution();
        Assert.assertNotNull("Data on demand for 'Institution' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Institution' failed to provide an identifier", id);
        obj = Institution.findInstitution(id);
        Assert.assertNotNull("Find method for 'Institution' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Institution' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void InstitutionIntegrationTest.testFindAllInstitutions() {
        Assert.assertNotNull("Data on demand for 'Institution' failed to initialize correctly", dod.getRandomInstitution());
        long count = Institution.countInstitutions();
        Assert.assertTrue("Too expensive to perform a find all test for 'Institution', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Institution> result = Institution.findAllInstitutions();
        Assert.assertNotNull("Find all method for 'Institution' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Institution' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void InstitutionIntegrationTest.testFindInstitutionEntries() {
        Assert.assertNotNull("Data on demand for 'Institution' failed to initialize correctly", dod.getRandomInstitution());
        long count = Institution.countInstitutions();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Institution> result = Institution.findInstitutionEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Institution' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Institution' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void InstitutionIntegrationTest.testFlush() {
        Institution obj = dod.getRandomInstitution();
        Assert.assertNotNull("Data on demand for 'Institution' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Institution' failed to provide an identifier", id);
        obj = Institution.findInstitution(id);
        Assert.assertNotNull("Find method for 'Institution' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyInstitution(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Institution' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void InstitutionIntegrationTest.testMergeUpdate() {
        Institution obj = dod.getRandomInstitution();
        Assert.assertNotNull("Data on demand for 'Institution' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Institution' failed to provide an identifier", id);
        obj = Institution.findInstitution(id);
        boolean modified =  dod.modifyInstitution(obj);
        Integer currentVersion = obj.getVersion();
        Institution merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Institution' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void InstitutionIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Institution' failed to initialize correctly", dod.getRandomInstitution());
        Institution obj = dod.getNewTransientInstitution(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Institution' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Institution' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Institution' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void InstitutionIntegrationTest.testRemove() {
        Institution obj = dod.getRandomInstitution();
        Assert.assertNotNull("Data on demand for 'Institution' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Institution' failed to provide an identifier", id);
        obj = Institution.findInstitution(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Institution' with identifier '" + id + "'", Institution.findInstitution(id));
    }
    
}
