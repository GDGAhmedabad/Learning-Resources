<?php
	$url = 'https://fcm.googleapis.com/fcm/send';
	$fields = array (
	  'to' => 'ci0k-ogrlLM:APA91bHcIvW6w6PP46Ddye9860UAFUD7gVrPNDPSyyCX65_-GYmEFkVO_qO-8ePq65KOEAFbtQgcxyp3YjzK6ufbMjg_ZF4A_wemkiZCWoENqiHFzdkusHJu9xcd44CsQ1dvfmQFF7YC',
	  "notification" => array(
	   "title"=> "Welcome To App",
	   "body"=> "App Description"
	  )
	);
	
	$fields = json_encode ( $fields );
	
	$headers = array (
  'Authorization: key=' . "AAAATnMHw10:APA91bH2jZJqwCWzaOpF3Mg98L0fPsc8WRLpiwNu_RI8UdPAMTZYJzztiqQIMITzlSCnHq-3c1-RD7CGz7E2TU2Wxjf4tcJvWz2zzOPaSfeuc_HQr2z3CISHx_gLimu0fXxyCR1SEo57",
  'Content-Type: application/json'
);


$ch = curl_init ();
curl_setopt ( $ch, CURLOPT_URL, $url );
curl_setopt ( $ch, CURLOPT_POST, true );
curl_setopt ( $ch, CURLOPT_HTTPHEADER, $headers );
curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, true );
curl_setopt ( $ch, CURLOPT_POSTFIELDS, $fields );

$result = curl_exec ( $ch );
echo $result;
curl_close ( $ch );
?>