package org.soap.ws.server.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;

import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.staxutils.PrettyPrintXMLStreamWriter;
import org.apache.cxf.staxutils.StaxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggingOutMessageInterceptor extends AbstractPhaseInterceptor<Message>{

	private static final Logger logger = LoggerFactory.getLogger(LoggingOutMessageInterceptor.class);
    private int limit = 48*1024;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public LoggingOutMessageInterceptor() {
        super(Phase.PRE_STREAM);
        addBefore(StaxOutInterceptor.class.getName());
    }

    public void handleMessage(Message message) throws Fault {
        message.put("org.apache.cxf.stax.force-start-document", Boolean.TRUE);
        OutputStream os = (OutputStream) message.getContent(OutputStream.class);
        if (os == null) {
            return;
        }
        CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
        message.setContent(OutputStream.class, newOut);
        newOut.registerCallback(new LoggingCallback(message, os));
    }

    class LoggingCallback implements CachedOutputStreamCallback {
        private final Message message;
        private final OutputStream origStream;

        public LoggingCallback(Message msg, OutputStream os) {
            this.message = msg;
            this.origStream = os;
        }

        public void onFlush(CachedOutputStream cos) {}

        public void onClose(CachedOutputStream cos) {
            /*String id = (String) this.message.getExchange().get(LoggingMessage.ID_KEY);
             if (id == null) {
             id = LoggingMessage.nextId();
             this.message.getExchange().put(LoggingMessage.ID_KEY, id);
             }*/

            SoapMessageUtils buffer = new SoapMessageUtils("Outbound Message\n---------------------------");

            String encoding = (String) this.message.get(Message.ENCODING);

            if (encoding != null) {
                buffer.getEncoding().append(encoding);
            }

            String address = (String) this.message.get(Message.ENDPOINT_ADDRESS);
            if (address != null) {
                buffer.getAddress().append(address);
            }
            String ct = (String) this.message.get("Content-Type");
            if (ct != null) {
                buffer.getContentType().append(ct);
            }
            Object headers = this.message.get(Message.PROTOCOL_HEADERS);
            if (headers != null) {
                buffer.getHeader().append(headers);
            }

            if (cos.getTempFile() == null) {
                if (cos.size() > LoggingOutMessageInterceptor.this.limit)
                    buffer.getMessage().append("(message truncated to " + LoggingOutMessageInterceptor.this.limit + " bytes)\n");
            } else {
                buffer.getMessage().append("Outbound Message (saved to tmp file):\n");
                buffer.getMessage().append("Filename: " + cos.getTempFile().getAbsolutePath() + "\n");
                if (cos.size() > LoggingOutMessageInterceptor.this.limit)
                    buffer.getMessage().append("(message truncated to " + LoggingOutMessageInterceptor.this.limit + " bytes)\n");
            }
            try {
                cos.writeCacheTo(buffer.getMessageLoad(), LoggingOutMessageInterceptor.this.limit);
            } catch (Exception ex) {
            }

            LoggingOutMessageInterceptor.writePrettyLog(buffer.getMessageLoad(), cos, encoding);
            logger.info("OUT:[{}]", buffer.toString());

            try {
                cos.lockOutputStream();
                cos.resetOut(null, false);
            } catch (Exception ex) {
            }
            this.message.setContent(OutputStream.class, this.origStream);
        }
    }

    public static void writePrettyLog(StringBuilder buffer, CachedOutputStream cos, String encoding) {
        StringWriter swriter = new StringWriter();
        XMLStreamWriter xwriter = StaxUtils.createXMLStreamWriter(swriter);
        xwriter = new PrettyPrintXMLStreamWriter(xwriter, 2);
        InputStream inputStream = null;
        try {
        	inputStream = cos.getInputStream();
            InputStreamReader inputStreamReader = StringUtils.isEmpty(encoding)
					? new InputStreamReader(inputStream)
					: new InputStreamReader(inputStream, encoding);
            StaxUtils.copy(new StreamSource(inputStreamReader), xwriter);
        } catch (Exception xse) {
            //ignore
        } finally {
            try {
                xwriter.flush();
                xwriter.close();
                inputStream.close();
            } catch (Exception xse2) {
                //ignore
            }
            
        }

        buffer.append(swriter.toString());
    }

    // 出现错误输出错误信息和栈信息
    public void handleFault(Message message) {
        Exception exeption = message.getContent(Exception.class);
        logger.debug("error message is:{}", exeption.getMessage());
    }
}
