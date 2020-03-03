<!DOCTYPE html>
<html>
<body>

    <h1>${title}</h1>
    
     <ul>
        <#list scores as score>
            <li>${score.player} ${score.score}
        </#list>
    </ul>
   

<form method="post">
  player: <input type="text" name="pname"><br>
  score:  <input type="text" name="pscore"><br>
  <button type="submit">Submit</button>
</form>
</body>
</html>

