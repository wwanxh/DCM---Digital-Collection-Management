# DCM---Digital-Collection-Management
***

**## ABOUT THIS PROJECT**

DCM is a digital documents organizing software, it take advantages of PDF/A format. A PDF/A file is trated as a container so that all associated information of a book, such as contents, images, texts, metadata, color profile, special notes, and attachment, all those can be embed in one single file. For documents grouped by titles, it loss-less compresses and converts raw textual documents from TIFF to JP2, embeds JP2 files into a single PDF/A2 file. Informations of XMP, ICC, special notes, metadata, and attachment(optional) are embedded along with images. In addition, DCM provides bidirectional conversions that supports both conversions from raw document to PDF/A2 and from PDF/A2 to raw document. 

The software has processed and tested around 1,5000 titles (roughly 1 million pages of books) at The University of Arizona Libraries for Afghanistan digital collections team.

The goals of this project were to (1) demostrate **[the research](https://ejournals.bc.edu/ojs/index.php/ital/article/view/9878/pdf)**, (2) increase storage efficiency in a large extent for digitized archivals, and (3) provides a concise and efficient orgnization so that easy access and management can be acheived. As a result, the project successfully demostrated the research; and the new structure saves up to 70% of storage for the same amount of documents compared to previous ones; documents can be easily managed and retrived.

The Project of Afghanistan digital collections Website:  http://www.afghandata.org

***

**## Document Conversion Patterns Supported:**

* A : TIFF -> 3 versions -> PDF/A 3B with Raws Moving (Lossless, lossy 50 30) 
* A1: TIFF -> 3 versions -> PDF/A 3B with Raws Copying (Lossless, lossy 50 30) 
* A2: TIFF -> 3 versions -> PDF/A 3B with Raws Remaining (Lossless, lossy 50 30) 
* B1: TIFF -> PDF/A 3B with RAWs Moving
* B2: TIFF -> PDF/A 3B with RAWs Copying
* B3: TIFF -> PDF/A 3B only 	*(Customized Quality Loss Level of Compression) //100-lossless, not linear for 0-99
* C1: PDF/A 3B -> JP2 -> TIFF (JP2 RETAINED)
* C2: PDF/A 3B -> TIFF (JP2 NOT RETAINED)


***

**## It has built several Add-ons that you may find something useful:**

* **Root XMP Editor:** `A tool to add dublin core and xmp properties into root level metadata of the generated PDF/A2`

* Text Layer Editor: Testing

* Hidden Layer Membership Support: Testing

***

**## INVOLVED RESEARCH**

**Abstract:**

> The purpose of this article is to demonstrate a practical use case of PDF/A file format for digitization of textual documents, following recommendation of using PDF/A as a preferred digitization file format. The authors showed how to convert and combine all the TIFFs with associated metadata into a single PDF/A-2b file for a document. Using open source software with real-life examples, the authors show readers how to convert TIFF images, extract associated metadata and ICC profiles, and validate against the newly released PDF/A validator. The generated PDF/A file is a self-contained and self-described container which accommodates all the data from digitization of textual materials, including page-level metadata and/or ICC profiles. With theoretical analysis and empirical examples, PDF/A file format has many advantages over traditional preferred file format TIFF / JPEG2000 for digitization of textual documents.

**Full Text:** 

**[CLICK HERE](https://ejournals.bc.edu/ojs/index.php/ital/article/view/9878/pdf)**

**Reference:**
> Han, Y., & Wan, X. (2018). Digitization of Textual Documents Using PDF/A. Information Technology and Libraries, 37(1), 52. doi:10.6017/ital.v37i1.9878


***
**## DEVELOPMENT UPDATES**

[Developer's Log](https://github.com/wwanxh/DCM---Digital-Collection-Management/wiki/Developer-Logs)


***
