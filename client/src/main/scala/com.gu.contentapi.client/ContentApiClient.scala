package com.gu.contentapi.client

import com.gu.contentapi.buildinfo.CapiBuildInfo
import com.gu.contentapi.client.model._
import com.gu.contentapi.client.model.v1._
import com.gu.contentapi.client.thrift.ThriftDeserializer
import com.gu.contentapi.client.utils.QueryStringParams
import scala.concurrent.{ExecutionContext, Future}

trait ContentApiClient {
  
  /** Your API key */
  def apiKey: String

  /** The user-agent identifier */
  def userAgent: String = "content-api-scala-client/"+CapiBuildInfo.version
  
  /** The url of the CAPI endpoint */
  def targetUrl: String = "https://content.guardianapis.com"

  /** Queries CAPI.
    *
    * This method must make a GET request to the CAPI endpoint
    * and streamline the response into an HttpResponse object.
    * 
    * It is a design decision that this method is virtual.
    * Any implementation would have to rely on a specific
    * technology stack, e.g. an HTTP client. Fundamentally,
    * the responsibility of making these implementation
    * choices should be pushed out to the end of the world.
    *
    * @param url The CAPI REST url 
    * @param headers Custom HTTP parameters
    * @return an HttpResponse holding the response in the form of an array of bytes 
    */
  def get(url: String, headers: Map[String, String])(implicit context: ExecutionContext): Future[HttpResponse]

  /** Some HTTP headers sent along each CAPI request */
  private def headers = Map("User-Agent" -> userAgent, "Accept" -> "application/x-thrift")

  /** Authentication and format parameters appended to each query */
  private def parameters = Map("api-key" -> apiKey, "format" -> "thrift")

  /** Streamlines the handling of a valid CAPI response */
  private def fetchResponse(contentApiQuery: ContentApiQuery)(implicit context: ExecutionContext): Future[Array[Byte]] =
    get(url(contentApiQuery), headers).flatMap(HttpResponse.check)

  /* Exposed API */

  def url(contentApiQuery: ContentApiQuery): String =
    contentApiQuery.getUrl(targetUrl, parameters)

  def getResponse(itemQuery: ItemQuery)(implicit context: ExecutionContext): Future[ItemResponse] =
    fetchResponse(itemQuery) map { response =>
      ThriftDeserializer.deserialize(response, ItemResponse)
    }

  def getResponse(searchQuery: SearchQueryBase[_])(implicit context: ExecutionContext): Future[SearchResponse] =
    fetchResponse(searchQuery) map { response =>
      ThriftDeserializer.deserialize(response, SearchResponse)
    }

  def getResponse(tagsQuery: TagsQuery)(implicit context: ExecutionContext): Future[TagsResponse] =
    fetchResponse(tagsQuery) map { response =>
      ThriftDeserializer.deserialize(response, TagsResponse)
    }

  def getResponse(sectionsQuery: SectionsQuery)(implicit context: ExecutionContext): Future[SectionsResponse] =
    fetchResponse(sectionsQuery) map { response =>
      ThriftDeserializer.deserialize(response, SectionsResponse)
    }

  def getResponse(editionsQuery: EditionsQuery)(implicit context: ExecutionContext): Future[EditionsResponse] =
    fetchResponse(editionsQuery) map { response =>
      ThriftDeserializer.deserialize(response, EditionsResponse)
    }

  def getResponse(removedContentQuery: RemovedContentQuery)(implicit context: ExecutionContext): Future[RemovedContentResponse] =
    fetchResponse(removedContentQuery) map { response =>
      ThriftDeserializer.deserialize(response, RemovedContentResponse)
    }

  def getResponse(videoStatsQuery: VideoStatsQuery)(implicit context: ExecutionContext): Future[VideoStatsResponse] =
    fetchResponse(videoStatsQuery) map { response =>
      ThriftDeserializer.deserialize(response, VideoStatsResponse)
    }

  def getResponse(atomsQuery: AtomsQuery)(implicit context: ExecutionContext): Future[AtomsResponse] =
    fetchResponse(atomsQuery) map { response =>
      ThriftDeserializer.deserialize(response, AtomsResponse)
    }

  def getResponse(recipesQuery: RecipesQuery)(implicit context: ExecutionContext): Future[AtomsResponse] =
    fetchResponse(recipesQuery) map { response =>
      ThriftDeserializer.deserialize(response, AtomsResponse)
    }

  def getResponse(reviewsQuery: ReviewsQuery)(implicit context: ExecutionContext): Future[AtomsResponse] =
    fetchResponse(reviewsQuery) map { response =>
      ThriftDeserializer.deserialize(response, AtomsResponse)
    }

  def getResponse(gameReviewsQuery: GameReviewsQuery)(implicit context: ExecutionContext): Future[AtomsResponse] =
    fetchResponse(gameReviewsQuery) map { response =>
      ThriftDeserializer.deserialize(response, AtomsResponse)
    }

  def getResponse(restaurantReviewsQuery: RestaurantReviewsQuery)(implicit context: ExecutionContext): Future[AtomsResponse] =
    fetchResponse(restaurantReviewsQuery) map { response =>
      ThriftDeserializer.deserialize(response, AtomsResponse)
    }

  def getResponse(filmReviewsQuery: FilmReviewsQuery)(implicit context: ExecutionContext): Future[AtomsResponse] =
    fetchResponse(filmReviewsQuery) map { response =>
      ThriftDeserializer.deserialize(response, AtomsResponse)
    }

  def getResponse(storiesQuery: StoriesQuery)(implicit context: ExecutionContext): Future[StoriesResponse] =
    fetchResponse(storiesQuery) map { response =>
      ThriftDeserializer.deserialize(response, StoriesResponse)
    }

}

object ContentApiClient extends ContentApiQueries

/** Utility functions to instantiate each type of query */
trait ContentApiQueries {
  def item(id: String) = ItemQuery(id)
  val search = SearchQuery()
  val tags = TagsQuery()
  val sections = SectionsQuery()
  val editions = EditionsQuery()
  val removedContent = RemovedContentQuery()
  val atoms = AtomsQuery()
  val recipes = RecipesQuery()
  val reviews = ReviewsQuery()
  val gameReviews = GameReviewsQuery()
  val restaurantReviews = RestaurantReviewsQuery()
  val filmReviews = FilmReviewsQuery()
  val videoStats = VideoStatsQuery()
  val stories = StoriesQuery()
}