package se.fishtank.css.selectors;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import se.fishtank.css.selectors.scanner.Scanner;
import se.fishtank.css.selectors.scanner.ScannerException;

public class ScannerTest {

    @Test
    public void checkSelectorGroups() throws ScannerException {
        Scanner scanner1 = new Scanner("div div div");
        List<List<Selector>> result1 = scanner1.scan();
        Assert.assertEquals(1, result1.size());
        
        Scanner scanner2 = new Scanner("div, div div");
        List<List<Selector>> result2 = scanner2.scan();
        Assert.assertEquals(2, result2.size());
        
        Scanner scanner3 = new Scanner("div, a, span");
        List<List<Selector>> result3 = scanner3.scan();
        Assert.assertEquals(3, result3.size());
        
        Scanner scanner4 = new Scanner("div, span div, span");
        List<List<Selector>> result4 = scanner4.scan();
        Assert.assertEquals(3, result4.size());
    }
    
}
