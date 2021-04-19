package com.springboot_vue.backend.service.impl;

import com.springboot_vue.backend.common.util.UserUtils;
import com.springboot_vue.backend.entity.Article;
import com.springboot_vue.backend.entity.Comment;
import com.springboot_vue.backend.repository.ArticleRepository;
import com.springboot_vue.backend.repository.CommentRepository;
import com.springboot_vue.backend.repository.UserRepository;
import com.springboot_vue.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private CommentRepository commentRepository;

	@Override
	public List<Comment> findAll() {
		return commentRepository.findAll();
	}

	@Override
	public Comment getCommentById(Integer id) {
		return commentRepository.getOne(id);
	}

	@Override
	@Transactional
	public Integer saveComment(Comment comment) {

		return commentRepository.save(comment).getId();
	}


	@Override
	@Transactional
	public void deleteCommentById(Integer id) {
		commentRepository.deleteById(id);
	}

	@Override
	public List<Comment> listCommentsByArticle(Integer id) {
		Article a = new Article();
		a.setId(id);
		return commentRepository.findByArticleAndLevelOrderByCreateDateDesc(a, "0");
	}

	@Override
	@Transactional
	public Comment saveCommentAndChangeCounts(Comment comment) {

		int count = 1;
		Optional<Article> a = articleRepository.findById(comment.getArticle().getId());

		a.ifPresent(article -> article.setCommentCounts(article.getCommentCounts() + count));

		comment.setAuthor(UserUtils.getCurrentUser());
		comment.setCreateDate(new Date());

		//设置level
		if (null == comment.getParent()) {
			comment.setLevel("0");
		} else {
			if (null == comment.getUser()) {
				comment.setLevel("1");
			} else {
				comment.setLevel("2");
			}
		}

		return commentRepository.save(comment);

	}

	@Override
	@Transactional
	public void deleteCommentByIdAndChangeCounts(Integer id) {
		int count = 1;
		Optional<Comment> c = commentRepository.findById(id);

		c.ifPresent(comment -> {
			comment.getArticle().setCommentCounts(comment.getArticle().getCommentCounts() - count);
			commentRepository.delete(comment);
		});
	}

}
