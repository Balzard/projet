package adri.suys.un_mutescan.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import adri.suys.un_mutescan.viewinterfaces.PayViewInterface;

import static org.junit.Assert.*;

public class PayPresenterTest {

    @Mock PayViewInterface viewInterface;
    private PayPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new PayPresenter(viewInterface);
    }

    @Test
    public void testEmailValidation1(){
        assertTrue(presenter.validateEmail("mon_email@gmail.com"));
    }

    @Test
    public void testEmailValidation2(){
        assertTrue(presenter.validateEmail("mon_email@gmail.co.uk"));
    }

    @Test
    public void testEmailValidation3(){
        assertFalse(presenter.validateEmail("mon_email.gmail.co.uk"));
    }

    @Test
    public void testEmailValidation4(){
        assertFalse(presenter.validateEmail("mon_email@gmail"));
    }

    @Test
    public void testEmailValidation5(){
        assertFalse(presenter.validateEmail("@gmail.com"));
    }

    @Test
    public void testEmailValidation6(){
        assertFalse(presenter.validateEmail(null));
    }
}