/**
 * OWASP Benchmark v1.2
 *
 * <p>This file is part of the Open Web Application Security Project (OWASP) Benchmark Project. For
 * details, please see <a
 * href="https://owasp.org/www-project-benchmark/">https://owasp.org/www-project-benchmark/</a>.
 *
 * <p>The OWASP Benchmark is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, version 2.
 *
 * <p>The OWASP Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * @author Dave Wichers
 * @created 2015
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/pathtraver-00/BenchmarkTest00001")
public class BenchmarkTest00001 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        javax.servlet.http.Cookie userCookie =
                new javax.servlet.http.Cookie("BenchmarkTest00001", "FileName");
        userCookie.setMaxAge(60 * 3); // Store cookie for 3 minutes
        userCookie.setSecure(true);
        userCookie.setHttpOnly(true);
        userCookie.setPath(request.getRequestURI());
        userCookie.setDomain(new java.net.URL(request.getRequestURL().toString()).getHost());
        response.addCookie(userCookie);
        javax.servlet.RequestDispatcher rd =
                request.getRequestDispatcher("/pathtraver-00/BenchmarkTest00001.html");
        if (rd != null) {
            rd.include(request, response);
        } else {
            response.getWriter().println("Problem getting RequestDispatcher: rd is null");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // some code
        response.setContentType("text/html;charset=UTF-8");

        javax.servlet.http.Cookie[] theCookies = request.getCookies();

        String param = "noCookieValueSupplied";
        if (theCookies != null) {
            for (javax.servlet.http.Cookie theCookie : theCookies) {
                if (theCookie.getName().equals("BenchmarkTest00001")) {
                    param = java.net.URLDecoder.decode(theCookie.getValue(), "UTF-8");
                    break;
                }
            }
        }

        String fileName = null;
        java.io.FileInputStream fis = null;

        try {
            fileName = org.owasp.benchmark.helpers.Utils.TESTFILES_DIR + param;
            if (fileName == null) {
                throw new java.io.FileNotFoundException("File name is null");
            }
            fis = new java.io.FileInputStream(new java.io.File(pathFilter(fileName)));
            byte[] b = new byte[1000];
            int size = fis.read(b);
            if (size != -1) {
                response.getWriter()
                        .println(
                                "The beginning of file is:\n\n"
                                        + org.owasp
                                                .esapi
                                                .ESAPI
                                                .encoder()
                                                .encodeForHTML(new String(b, 0, size)));
            } else {
                response.getWriter()
                        .println(
                                "The beginning of file is empty.");
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Couldn't open FileInputStream");
            response.getWriter()
                    .println(
                            "Problem getting FileInputStream: File not found");
        } catch (IOException e) {
            System.out.println("Error reading or processing file");
            response.getWriter()
                    .println(
                            "Problem getting FileInputStream: IO Error occurred");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred");
            response.getWriter()
                    .println(
                            "Problem getting FileInputStream: An error occurred");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    fis = null;
                } catch (IOException e) {
                    System.out.println("Error closing FileInputStream");
                }
            }
        }
    }

    private static String pathFilter(String path) {
        if (path == null) {
            return null;
        }
        String baseDir = org.owasp.benchmark.helpers.Utils.TESTFILES_DIR;
        String param = "";
        
        String normalizedPath = path.replace("\\", "/");
        String normalizedBaseDir = baseDir.replace("\\", "/");
        
        if (normalizedPath.startsWith(normalizedBaseDir)) {
            param = path.substring(baseDir.length());
        } else {
            param = path;
        }
        
        String safeParam = "file1.txt";
        if (param != null) {
            switch (param) {
                case "file1.txt":
                    safeParam = "file1.txt";
                    break;
                case "file2.txt":
                    safeParam = "file2.txt";
                    break;
                default:
                    safeParam = "file1.txt";
                    break;
            }
        }
        
        return baseDir + safeParam;
    }
}
