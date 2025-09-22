<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Diagnostic Servlet PrimesServlet</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
      line-height: 1.6;
      max-width: 1000px;
    }
    h1, h2 {
      color: #333;
    }
    .test-section {
      margin-bottom: 30px;
      padding: 20px;
      border: 1px solid #ddd;
      border-radius: 5px;
      background-color: #f9f9f9;
    }
    .form-group {
      margin-bottom: 15px;
    }
    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    input, select, button {
      padding: 8px 12px;
      font-size: 14px;
      border: 1px solid #ccc;
      border-radius: 4px;
      margin-bottom: 10px;
    }
    button {
      background-color: #4CAF50;
      color: white;
      cursor: pointer;
      border: none;
      margin-right: 10px;
    }
    button:hover {
      background-color: #45a049;
    }
    button#testPath {
      background-color: #2196F3;
    }
    .instructions {
      background-color: #fff4e6;
      padding: 15px;
      border-left: 4px solid #ffa94d;
      margin: 20px 0;
    }
    .error {
      color: #d32f2f;
      font-weight: bold;
    }
    .success {
      color: #388e3c;
    }
    .result {
      margin-top: 20px;
      padding: 15px;
      background-color: #f5f5f5;
      border: 1px solid #ddd;
      border-radius: 4px;
      white-space: pre-wrap;
      font-family: monospace;
    }
    .solution {
      background-color: #e8f5e9;
      padding: 15px;
      border-left: 4px solid #4CAF50;
      margin: 20px 0;
    }
    ul {
      padding-left: 20px;
    }
    li {
      margin-bottom: 10px;
    }
  </style>
</head>
<body>
<h1>Diagnostic de l'erreur 404 - Servlet PrimesServlet</h1>

<div class="instructions">
  <p><strong>Problème:</strong> Le serveur retourne une erreur 404 (Not Found) lorsque vous essayez d'accéder au servlet PrimesServlet.</p>
  <p><strong>Solution:</strong> Utilisez les outils ci-dessous pour tester différentes URL et diagnostiquer le problème.</p>
</div>

<div class="test-section">
  <h2>Test d'URL alternatives</h2>

  <div class="form-group">
    <label for="contextPath">Chemin de contexte de l'application:</label>
    <input type="text" id="contextPath" value="${pageContext.request.contextPath}" placeholder="/votre-application">
    <small>Généralement le nom de votre application web</small>
  </div>

  <div class="form-group">
    <label for="servletPath">Chemin du servlet:</label>
    <input type="text" id="servletPath" value="/PrimesServlet" placeholder="/PrimesServlet">
  </div>

  <button id="testPath">Tester cette URL</button>
  <button id="testAll">Tester toutes les URLs courantes</button>

  <div id="testResults" class="result">Les résultats des tests s'afficheront ici.</div>
</div>

<div class="solution">
  <h2>Solutions possibles pour résoudre l'erreur 404:</h2>
  <ul>
    <li><strong>Vérifiez l'URL complète:</strong> L'URL doit être de la forme <code>http://localhost:8080/votre-application/PrimesServlet</code></li>
    <li><strong>Vérifiez l'annotation @WebServlet:</strong> Assurez-vous que le servlet est bien annoté avec <code>@WebServlet("/PrimesServlet")</code></li>
    <li><strong>Vérifiez le mapping dans web.xml:</strong> Si vous utilisez web.xml, assurez-vous que le servlet est correctement mappé</li>
    <li><strong>Redéployez l'application:</strong> Parfois, un redéploiement complet peut résoudre le problème</li>
    <li><strong>Vérifiez les logs du serveur:</strong> Les logs peuvent contenir des informations sur d'éventuelles erreurs de déploiement</li>
  </ul>
</div>

<script>
  document.getElementById('testPath').addEventListener('click', function() {
    const contextPath = document.getElementById('contextPath').value;
    const servletPath = document.getElementById('servletPath').value;
    const fullUrl = contextPath + servletPath + '?type=interieur';

    testUrl(fullUrl);
  });

  document.getElementById('testAll').addEventListener('click', function() {
    const contextPath = document.getElementById('contextPath').value;
    const testUrls = [
      contextPath + '/PrimesServlet?type=interieur',
      contextPath + '/primesServlet?type=interieur',
      contextPath + '/primes?type=interieur',
      '/PrimesServlet?type=interieur',
      '/primesServlet?type=interieur'
    ];

    const resultsDiv = document.getElementById('testResults');
    resultsDiv.innerHTML = 'Test de toutes les URLs...\n\n';

    testUrls.forEach(url => {
      testUrl(url, true);
    });
  });

  function testUrl(url, append = false) {
    const resultsDiv = document.getElementById('testResults');
    if (!append) {
      resultsDiv.innerHTML = 'Test de l\'URL: ' + url + '\n\n';
    }

    fetch(url)
            .then(response => {
              let resultText = `URL: ${url}\n`;
              resultText += `Status: ${response.status} ${response.statusText}\n`;

              if (response.ok) {
                resultText += `✅ SUCCÈS: Le servlet a répondu correctement\n`;
                return response.json().then(data => {
                  resultText += `Données: ${JSON.stringify(data)}\n`;
                  resultsDiv.innerHTML += resultText + '\n';
                });
              } else {
                resultText += `❌ ERREUR: ${response.status} - ${response.statusText}\n`;
                resultsDiv.innerHTML += resultText + '\n';
              }
            })
            .catch(error => {
              resultsDiv.innerHTML += `URL: ${url}\n❌ ERREUR: ${error.message}\n\n`;
            });
  }

  // Test initial
  const initialUrl = "${pageContext.request.contextPath}/PrimesServlet?type=interieur";
  document.getElementById('testResults').innerHTML = 'Test initial en cours...\n\n';
  testUrl(initialUrl);
</script>
</body>
</html>