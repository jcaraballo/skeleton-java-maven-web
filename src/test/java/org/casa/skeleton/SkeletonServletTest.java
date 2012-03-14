package org.casa.skeleton;

import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SkeletonServletTest {

    private final HttpServletResponse response = mockedHttpServletResponse();

    @Test
    public void respondsOk() throws IOException, ServletException {

        new SkeletonServlet().doGet(mock(HttpServletRequest.class), response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    private static HttpServletResponse mockedHttpServletResponse() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        try {
            when(response.getWriter()).thenReturn(mock(PrintWriter.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}