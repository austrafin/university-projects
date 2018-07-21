<?php
include_once 'common.inc.php';

$xmlstring = '<?xml version="1.0"?>';
$xmlstring .= '<sitemap>';
$handle = opendir($fileDir);
$beerNotes = array();
$pages = array();

while (($file = readdir($handle)) !== FALSE)
{
	if (is_dir($fileDir . $file)) continue;
	
	if (preg_match("/^(Beer Note).*\.xml$/", $file))
	{
		$xmlItem = simplexml_load_file($fileDir . $file);
		
		if ((string)$xmlItem->status == 'live') {
			$id = "index.php?id=" . (string)$xmlItem['id'];
			$type = "Beer Note";
			$created = preg_replace("/[^0-9]/", '', $id);
			$xmlstring .= '<content id="' . $id . '">';
			$xmlstring .= '<name>' . htmlentities($xmlItem->note_content->name) . '</name>';
			$xmlstring .= '<type>' . $type . '</type>';
			$xmlstring .= '<created>' . $created . '</created>';
			$xmlstring .= '</content>';
		}
	}
	else if(preg_match("/^(pages).*\.xml$/", $file))
	{
		$xmlItem = simplexml_load_file($fileDir . $file);
		
		foreach($xmlItem->page as $page)
		{
			if ($page->status == 'live') {
				$id = $page->url;
				$type = "Main Page";
				$created = preg_replace("/[^0-9]/", '', $id);
				$xmlstring .= '<content id="' . $id . '">';
				$xmlstring .= '<name>' . htmlentities($page->name) . '</name>';
				$xmlstring .= '<type>' . $type . '</type>';
				$xmlstring .= '<created>' . $created . '</created>';
				$xmlstring .= '</content>';
			}
			
			foreach($page as $subPage)
			{
				if ($subPage->status == 'live') {
					$id = $subPage->url;
					$type = "Sub Page";
					$created = preg_replace("/[^0-9]/", '', $id);
					$xmlstring .= '<content id="' . $id . '">';
					$xmlstring .= '<name>' . htmlentities($subPage->name) . '</name>';
					$xmlstring .= '<type>' . $type . '</type>';
					$xmlstring .= '<created>' . $created . '</created>';
					$xmlstring .= '</content>';
				}
			}
		}
	}
}
$xmlstring .= '</sitemap>';

if (isset($_GET['sortby'])) {
	$sortby = $_GET['sortby'];
} else {
	$sortby = 'name';
}

$xml = simplexml_load_string($xmlstring);
$xsl = new DOMDocument;
$xsl->load('xslt/sitemap.xsl');
$proc = new XSLTProcessor;
$proc->importStyleSheet($xsl);
$proc->setParameter('', 'SORTBY', $sortby);
echo $proc->transformToXML($xml);
?>
