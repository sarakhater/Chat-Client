<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body background="##6BD2DB">
  <h2>My message History</h2>
  <table border="2">
    <tr bgcolor="#9acd32">
      <th>Sender</th>
      <th>Message</th>
    </tr>
    <xsl:for-each select="mychat/message">
    <tr>
      <td><xsl:value-of select="attribute::sender"/></td>
      <td><xsl:value-of select="."/></td>
    </tr>
    </xsl:for-each>
  </table>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>