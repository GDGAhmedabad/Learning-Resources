<?php
	$news = array(
		array("title"=>$_POST['title'],"content"=>$_POST['content']),
		array("title"=>"News Title 1","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
		array("title"=>"News Title 2","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
		array("title"=>"News Title 3","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
		array("title"=>"News Title 4","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
		array("title"=>"News Title 5","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
		array("title"=>"News Title 6","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
		array("title"=>"News Title 7","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
		array("title"=>"News Title 8","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
		array("title"=>"News Title 9","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
		array("title"=>"News Title 10","content"=>"Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
	);
	echo json_encode(array("news"=>$news));
?>