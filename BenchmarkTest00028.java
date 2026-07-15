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

@WebServlet(value = "/pathtraver-00/BenchmarkTest00028")
public class BenchmarkTest00028 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // some code
        response.setContentType("text/html;charset=UTF-8");

        java.util.Map<String, String[]> map = request.getParameterMap();
        String param = "";
        if (!map.isEmpty()) {
            String[] values = map.get("BenchmarkTest00028");
            if (values != null) param = values[0];
        }

        String fileName = null;
        java.io.FileOutputStream fos = null;

        try {
            fileName = org.owasp.benchmark.helpers.Utils.TESTFILES_DIR + param;
            if (fileName == null) {
                throw new java.io.FileNotFoundException("File name is null");
            }
            fos = new java.io.FileOutputStream(pathFilter(fileName), false);
            response.getWriter()
                    .println("Now ready to write to file.");

        } catch (java.io.FileNotFoundException e) {
            System.out.println("Couldn't open FileOutputStream");
            response.getWriter()
                    .println(
                            "Problem getting FileOutputStream: File not found");
        } catch (IOException e) {
            System.out.println("Error writing or processing file");
            response.getWriter()
                    .println(
                            "Problem getting FileOutputStream: IO Error occurred");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    System.out.println("Error closing FileOutputStream");
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
