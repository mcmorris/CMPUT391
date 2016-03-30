CREATE OR REPLACE PROCEDURE getRankedPics(pSubject, pPlace, pDesc, pMode, pUser)
BEGIN
	/* Public - access */
	if (mode == 1) {
		SELECT photo_id INTO accessiblePics
		FROM photos;
	}
	/* Private - access only if owner or admin */
	else if (mode == 2) {
		SELECT owner_name, photo_id INTO accessiblePics
		FROM photos
		WHERE owner_name = pUser OR owner_name = 'admin';		
	}
	/* Must be member or owner of gl or admin. */
	else {
		SELECT photo_id INTO accessiblePics
		FROM photos
		WHERE owner_name = pUser OR owner_name = 'admin' OR
		permitted = ANY(SELECT group_id 
			FROM group_lists
			WHERE pUser = friend_id);
	}
	
	/* Rank(photo_id) = 6*frequency(subject) + 3*frequency(place) + frequency(description) */
  	SELECT photo_id, 6*SCORE(1) + 3*SCORE(2) + SCORE(3) AS rnk INTO rankedPics FROM accessiblePics
	WHERE CONTAINS(subject, 'dog', 1) > 0 OR CONTAINS(place, 'dick', 2) > 0 OR CONTAINS(description, 'ass', 3)
	ORDER BY (6*SCORE(1) + 3*SCORE(2) + SCORE(3)) DESC;
	
	/* Only supposed to return the top 5 ranked photos. */
	SELECT photo_id
	FROM rankedPics
	WHERE ROWNUM <= 5;
	
END getRankedPics;
/
